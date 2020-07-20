package com.passenger.financial.service.statistical;

import com.passenger.financial.common.CommonConstant;
import com.passenger.financial.entity.Driver;
import com.passenger.financial.entity.Organization;
import com.passenger.financial.entity.TurnoverRecord;
import com.passenger.financial.mapper.DriverMapper;
import com.passenger.financial.mapper.OrganizationMapper;
import com.passenger.financial.mapper.TurnoverRecordMapper;
import com.passenger.financial.service.excel.ExcelService;
import com.passenger.financial.utils.CalculateUtil;
import com.passenger.financial.vo.StatisticalInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticalService {

    @Resource
    private DriverMapper driverMapper;

    @Resource
    private TurnoverRecordMapper turnoverRecordMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private ExcelService excelService;

    @Value("${statistical.file.path}")
    private String filePath;

    public StatisticalInfo getStatisticalPreInfo(String currentDate, String accountingOrganizationId) {

        StatisticalInfo statisticalInfo = new StatisticalInfo();
        //查询当前核算组织下所有的司机人数
        Integer accountingId = Integer.valueOf(accountingOrganizationId);
        Organization organization = organizationMapper.findById(accountingId);
        int totalCount = driverMapper.findByAccountIdCount(accountingId);
        //查询当前核算组织下当日司机提交的营收记录
        List<TurnoverRecord> records = turnoverRecordMapper.findAllRecordByDateAndAccountId(currentDate,accountingId);

        //查询当前核算组织下的直属组织 目前只支持两级
        List<Organization> directOrganizations = organizationMapper.findByParentId(accountingId);
        statisticalInfo.setStatisticalDate(currentDate);
        buildPreStatisticalInfo(statisticalInfo,records,directOrganizations);
        //构建当前信息
        //查询未提交的司机信息
        List<Driver> noCommitDrivers = driverMapper.findNoCommitInfo(accountingId,currentDate);
        statisticalInfo.setOrganizationId(accountingId);
        statisticalInfo.setOrganizationName(organization.getOrganizationName());
        statisticalInfo.setTotalCount(totalCount);
        statisticalInfo.setNoCommitCount(CollectionUtils.isEmpty(noCommitDrivers) ? 0 : noCommitDrivers.size());
        statisticalInfo.setNoCommitDriverInfos(noCommitDrivers);
        statisticalInfo.setDistributionAmount(CalculateUtil.div(statisticalInfo.getTotalProfits(),statisticalInfo.getOperationCount()));

        List<StatisticalInfo> directOrganizationInfos = statisticalInfo.getDirectOrganizationInfos();
        for (StatisticalInfo directOrganizationInfo : directOrganizationInfos) {
            if (!"0".equals(directOrganizationInfo.getOperationCount())){
                directOrganizationInfo.setDistributionAmount(CalculateUtil.div(directOrganizationInfo.getTotalProfits(),directOrganizationInfo.getOperationCount()));
            }
        }
        return statisticalInfo;
    }

    private void buildPreStatisticalInfo(StatisticalInfo statisticalInfo, List<TurnoverRecord> records, List<Organization> directOrganizations) {
        if (CollectionUtils.isEmpty(directOrganizations)){
            return ;
        }

        Map<Integer, StatisticalInfo> directOrganizationInfoMap = new HashMap<>(directOrganizations.size());
        Map<Integer,Organization> organizationMap = new HashMap<>(directOrganizations.size());
        for (Organization directOrganization : directOrganizations) {
            directOrganizationInfoMap.put(directOrganization.getId(),new StatisticalInfo());
            organizationMap.put(directOrganization.getId(),directOrganization);
        }

        int leaveCount = 0 ;

        int dutyCount = 0;

        int lowCount = 0;

        String totalIncome = "0";

        String totalSpending = "0";

        String totalProfits = "0";

        String operationCount = "0";

        if(CollectionUtils.isEmpty(records)){
            return ;
        }
        for (TurnoverRecord record : records) {
            if (record.getType() == CommonConstant.WORK){
                //上班记录
                totalIncome = CalculateUtil.add(totalIncome,record.getIncome());
                totalSpending = CalculateUtil.add(totalSpending,record.getSpending());
                totalProfits = CalculateUtil.add(totalProfits,record.getProfits());
                operationCount = CalculateUtil.add(operationCount,record.getWorkTimeValue());
                if (!CollectionUtils.isEmpty(directOrganizations)){
                    //构建直接组织相关信息
                    StatisticalInfo directStatisticalInfo = directOrganizationInfoMap.get(record.getOrganizationId());
                    directStatisticalInfo.setTotalIncome(CalculateUtil.add(directStatisticalInfo.getTotalIncome(),record.getIncome()));
                    directStatisticalInfo.setTotalSpending(CalculateUtil.add(directStatisticalInfo.getTotalSpending(),record.getSpending()));
                    directStatisticalInfo.setTotalProfits(CalculateUtil.add(directStatisticalInfo.getTotalProfits(),record.getProfits()));
                    directStatisticalInfo.setOperationCount(CalculateUtil.add(directStatisticalInfo.getOperationCount(),record.getWorkTimeValue()));
                    directStatisticalInfo.setOrganizationId(record.getOrganizationId());
                    directStatisticalInfo.setOrganizationName(organizationMap.get(record.getOrganizationId()).getOrganizationName());
                }

            }else if(record.getType() == CommonConstant.DUTY){
                dutyCount++;
                operationCount = CalculateUtil.add(operationCount,record.getWorkTimeValue());
                if (!CollectionUtils.isEmpty(directOrganizations)){
                    StatisticalInfo directStatisticalInfo = directOrganizationInfoMap.get(record.getOrganizationId());
                    directStatisticalInfo.setDutyCount(directStatisticalInfo.getDutyCount() + 1);
                    directStatisticalInfo.setOperationCount(CalculateUtil.add(directStatisticalInfo.getOperationCount(),record.getWorkTimeValue()));
                    directStatisticalInfo.setOrganizationId(record.getOrganizationId());
                    directStatisticalInfo.setOrganizationName(organizationMap.get(record.getOrganizationId()).getOrganizationName());
                }

            }else if (record.getType() == CommonConstant.LOW){
                lowCount++;
                operationCount = CalculateUtil.add(operationCount,record.getWorkTimeValue());
                if (!CollectionUtils.isEmpty(directOrganizations)){
                    StatisticalInfo directStatisticalInfo = directOrganizationInfoMap.get(record.getOrganizationId());
                    directStatisticalInfo.setLowCount(directStatisticalInfo.getLowCount() + 1);
                    directStatisticalInfo.setOperationCount(CalculateUtil.add(directStatisticalInfo.getOperationCount(),record.getWorkTimeValue()));
                    directStatisticalInfo.setOrganizationId(record.getOrganizationId());
                    directStatisticalInfo.setOrganizationName(organizationMap.get(record.getOrganizationId()).getOrganizationName());
                }
            }else if (record.getType() == CommonConstant.LEAVE){
                leaveCount++;
                if (!CollectionUtils.isEmpty(directOrganizations)){
                    StatisticalInfo directStatisticalInfo = directOrganizationInfoMap.get(record.getOrganizationId());
                    directStatisticalInfo.setLeaveCount(directStatisticalInfo.getLeaveCount()+1);
                    directStatisticalInfo.setOrganizationId(record.getOrganizationId());
                    directStatisticalInfo.setOrganizationName(organizationMap.get(record.getOrganizationId()).getOrganizationName());
                }
            }
        }

        statisticalInfo.setTotalIncome(totalIncome);
        statisticalInfo.setTotalSpending(totalSpending);
        statisticalInfo.setTotalProfits(totalProfits);
        statisticalInfo.setOperationCount(operationCount);
        statisticalInfo.setDutyCount(dutyCount);
        statisticalInfo.setLeaveCount(leaveCount);
        statisticalInfo.setLowCount(lowCount);

        List<StatisticalInfo> directOrganizationInfos = new ArrayList<>(directOrganizations.size());

        for (Map.Entry<Integer,StatisticalInfo> entry :directOrganizationInfoMap.entrySet()){
            directOrganizationInfos.add(entry.getValue());
        }
        statisticalInfo.setDirectOrganizationInfos(directOrganizationInfos);
    }

    public Map<String,String> statistical(String currentDate, String accountingOrganizationId) {
        Map<String,String> resultMap = new HashMap<>();
        int accountingId = Integer.valueOf(accountingOrganizationId);
        //查询未提交的司机信息
        List<Driver> noCommitDrivers = driverMapper.findNoCommitInfo(accountingId,currentDate);
        if (!CollectionUtils.isEmpty(noCommitDrivers)){
            resultMap.put("msg","还要司机未提交当日记录，请联系司机提交后再统计");
            return resultMap;
        }
        StatisticalInfo statisticalPreInfo = getStatisticalPreInfo(currentDate, accountingOrganizationId);

        //查询当前核算组织下当日司机提交的营收记录
        List<TurnoverRecord> records = turnoverRecordMapper.findAllRecordByDateAndAccountId(currentDate,accountingId);

        Map<Integer,List<TurnoverRecord>> recordMap = new HashMap<>();

        for (TurnoverRecord record : records) {
            //更新日均分配和应收金额
            record.setDistributionAmount(statisticalPreInfo.getDistributionAmount());
            record.setShouldAmount(CalculateUtil.multiply(record.getDistributionAmount(),record.getWorkTimeValue()));
            turnoverRecordMapper.update(record);
            List<TurnoverRecord> organizationRecords = recordMap.get(record.getOrganizationId());
            if (CollectionUtils.isEmpty(organizationRecords)){
                organizationRecords = new ArrayList<>();
                recordMap.put(record.getOrganizationId(),organizationRecords);
            }
            organizationRecords.add(record);
        }
        buildExcel(statisticalPreInfo, recordMap, currentDate);
        resultMap.put("msg",CommonConstant.SUCCESS);
        resultMap.put("filePath",filePath + currentDate + "日统计表.xls");
        return resultMap;
    }

    private void buildExcel(StatisticalInfo statisticalPreInfo, Map<Integer, List<TurnoverRecord>> recordMap,String currentDate) {
        HSSFWorkbook hssfWorkbook = excelService.generateExcel(statisticalPreInfo, recordMap);
        String fileName = filePath + currentDate + "日统计表.xls";
        excelService.writeFile(hssfWorkbook,fileName);
    }
}
