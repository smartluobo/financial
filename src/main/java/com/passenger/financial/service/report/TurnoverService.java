package com.passenger.financial.service.report;

import com.passenger.financial.common.CommonConstant;
import com.passenger.financial.entity.Driver;
import com.passenger.financial.entity.Organization;
import com.passenger.financial.entity.TurnoverRecord;
import com.passenger.financial.mapper.DriverMapper;
import com.passenger.financial.mapper.OrganizationMapper;
import com.passenger.financial.mapper.TurnoverRecordMapper;
import com.passenger.financial.utils.CalculateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class TurnoverService {

    @Resource
    private DriverMapper driverMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private TurnoverRecordMapper turnoverRecordMapper;

    public TurnoverRecord findTurnoverRecord(String currentDate, String phone) {
        return turnoverRecordMapper.findRecordByDateAndPhone(currentDate,phone);
    }

    public String checkRecord(TurnoverRecord record) {

        try {
            Double.parseDouble(record.getIncome());
        }catch (Exception e){
            return "毛收入有误，请重新输入";
        }

        try {
            Double.parseDouble(record.getSpending());
        }catch (Exception e){
            return "油费输入，请重新输入";
        }
        return CommonConstant.SUCCESS;
    }

    public String initRecord(TurnoverRecord record) {
        int driverId = record.getDriverId();
        Driver driver = driverMapper.findById(driverId);

        Organization organization = organizationMapper.findById(driver.getOrganizationId());
        record.setDriverName(driver.getName());
        record.setPhone(driver.getPhone());
        record.setOrganizationId(organization.getId());
        record.setOrganizationName(organization.getOrganizationName());
        if (organization.getId() != driver.getAccountingOrganizationId()){
            organization =  organizationMapper.findById(driver.getAccountingOrganizationId());
        }
        if (organization == null){
            return "当前司机没有核算组织，请联系系统管理员";
        }
        record.setAccountingOrganizationId(organization.getId());
        record.setAccountingOrganizationName(organization.getOrganizationName());
        if (1 == record.getWorkTimeType()){
            record.setWorkTimeValue("1");
        }else{
            record.setWorkTimeValue("0.5");
        }
        //计算纯收入
        record.setProfits(CalculateUtil.sub(record.getIncome(),record.getSpending()));
        record.setUpdateTime(new Date());
        return CommonConstant.SUCCESS;
    }

    public void insert(TurnoverRecord record) {
        turnoverRecordMapper.insert(record);
    }

    public void update(TurnoverRecord record) {
        turnoverRecordMapper.update(record);
    }

    public Map<String,Object> initUpdateRecord(TurnoverRecord recordReq) {
        Map<String,Object> resultMap = new HashMap<>();
        TurnoverRecord oldRecord = turnoverRecordMapper.findById(recordReq.getId());
        if (oldRecord == null){
            resultMap.put("msg","更新记录，id不能为空");
            return resultMap;
        }
        oldRecord.setIncome(recordReq.getIncome());
        oldRecord.setSpending(recordReq.getSpending());
        oldRecord.setWorkTimeType(recordReq.getWorkTimeType());
        oldRecord.setType(recordReq.getType());
        if (1 == recordReq.getWorkTimeType()){
            oldRecord.setWorkTimeValue("1");
        }else{
            oldRecord.setWorkTimeValue("0.5");
        }
        //计算纯收入
        oldRecord.setProfits(CalculateUtil.sub(recordReq.getIncome(),recordReq.getSpending()));
        oldRecord.setUpdateTime(new Date());
        resultMap.put("msg",CommonConstant.SUCCESS);
        resultMap.put("record",oldRecord);
        return resultMap;
    }
}
