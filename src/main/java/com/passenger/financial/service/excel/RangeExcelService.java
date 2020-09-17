package com.passenger.financial.service.excel;

import com.passenger.financial.common.CommonConstant;
import com.passenger.financial.entity.Driver;
import com.passenger.financial.entity.StatisticalInfo;
import com.passenger.financial.entity.TurnoverRecord;
import com.passenger.financial.mapper.DriverMapper;
import com.passenger.financial.mapper.StatisticalRecordMapper;
import com.passenger.financial.mapper.TurnoverRecordMapper;
import com.passenger.financial.utils.CalculateUtil;
import com.passenger.financial.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RangeExcelService {

    @Resource
    private DriverMapper driverMapper;

    @Resource
    private TurnoverRecordMapper turnoverRecordMapper;

    @Resource
    private StatisticalRecordMapper statisticalRecordMapper;

    public HSSFWorkbook generateRangeExcel(String startTime,String endTime,int organizationId) throws Exception{

        String[] titles = generateTitles(startTime,endTime);

        HSSFWorkbook sheets = buildRangeExcel(startTime, endTime, titles, organizationId);
        return sheets;
    }

    private HSSFWorkbook buildRangeExcel(String startTime, String endTime, String[] titles, int organizationId) {
        //创建一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        //excel 样式
        HSSFCellStyle headStyle = workbook.createCellStyle();
        sheet.autoSizeColumn(1, true);
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        HSSFFont headFont = workbook.createFont();
        headFont.setFontHeightInPoints((short) 18);//设置字体大小
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        headStyle.setFont(headFont);


        //合并第一行显示
        CellRangeAddress statisticalTile = new CellRangeAddress(0, 0, 0, titles.length);
        sheet.addMergedRegion(statisticalTile);
        HSSFRow row = sheet.createRow(0);
        HSSFCell supperRow = row.createCell(0);
        supperRow.setCellValue(startTime+"到"+endTime+"分配统计表");
        supperRow.setCellStyle(headStyle);

        //创建表头
        Row headRow = sheet.createRow(1);
        for (int i = 0; i < titles.length; i++) {
            headRow.createCell(i).setCellValue(titles[i]);
        }
        int rowIndex = 2;

        //构建excel体

        String globalShouldAmount = "0";

        List<Integer> driverIds  = driverMapper.findIdByAccountId(organizationId);
        if (CollectionUtils.isEmpty(driverIds)){

            for (Integer driverId : driverIds) {
                List<TurnoverRecord> records = turnoverRecordMapper.findRecordByDriverId(driverId);
                if (!CollectionUtils.isEmpty(records)){
                    HSSFRow bodyRow = sheet.createRow(rowIndex++);
                    bodyRow.createCell(0).setCellValue(records.get(0).getDriverName());
                    int cellIndex = 1;
                    //分配总额
                    String distributionTotal = "0";
                    //上班天数 记录
                    String workDay = "0";
                    //个人收入 金额
                    String totalProfits = "0";
                    //个人应收 金额
                    String totalShouldIncome = "0";
                    //进 金额
                    String totalIn = "0";
                    //补 金额
                    String totalOut = "0";
                    for (TurnoverRecord record : records) {
                        if (record.getType() == CommonConstant.LEAVE){
                            bodyRow.createCell(cellIndex++).setCellValue("请假");
                        }else{
                            bodyRow.createCell(cellIndex++).setCellValue(record.getProfits());
                        }
                        distributionTotal = CalculateUtil.add(distributionTotal,record.getDistributionAmount());
                        workDay = CalculateUtil.add(workDay,record.getWorkTimeValue());
                        totalProfits = CalculateUtil.add(totalProfits,record.getProfits());
                        totalShouldIncome = CalculateUtil.add(totalShouldIncome,record.getShouldAmount());
                    }
                    if (Double.parseDouble(totalProfits) > Double.parseDouble(totalShouldIncome)){
                        //个人实际收入大于应该收入
                        totalOut = CalculateUtil.sub(totalProfits,totalShouldIncome);
                    }else{
                        totalIn = CalculateUtil.sub(totalShouldIncome,totalProfits);
                    }
                    //分配总额
                    bodyRow.createCell(cellIndex++).setCellValue(distributionTotal);
                    //上班天数 记录
                    bodyRow.createCell(cellIndex++).setCellValue(workDay);
                    //个人收入 金额
                    bodyRow.createCell(cellIndex++).setCellValue(totalProfits);
                    //个人应收 金额
                    bodyRow.createCell(cellIndex++).setCellValue(totalShouldIncome);
                    //进 金额
                    bodyRow.createCell(cellIndex++).setCellValue(totalIn);
                    //补 金额
                    bodyRow.createCell(cellIndex++).setCellValue(totalOut);
                    globalShouldAmount = CalculateUtil.add(globalShouldAmount,totalShouldIncome);
                }
            }
            List<StatisticalInfo> statisticalInfos =  statisticalRecordMapper.findStatisticalRecordByRange(startTime,endTime,organizationId);

            String globalProfits = "0";
            int summaryIndex = 0;
            //总收入一栏
            HSSFRow totalProfitsRow = sheet.createRow(rowIndex++);
            totalProfitsRow.createCell(0).setCellValue("总收入");
            //参营车辆一栏
            HSSFRow totalOperation = sheet.createRow(rowIndex++);
            totalOperation.createCell(0).setCellValue("参营车辆");
            //每日分配一栏
            HSSFRow totalDistribution = sheet.createRow(rowIndex++);
            totalDistribution.createCell(0).setCellValue("每日分配");
            for (StatisticalInfo statisticalInfo : statisticalInfos) {
                summaryIndex++;
                totalProfitsRow.createCell(summaryIndex).setCellValue(statisticalInfo.getTotalProfits());
                totalOperation.createCell(summaryIndex).setCellValue(statisticalInfo.getOperationCount());
                totalDistribution.createCell(summaryIndex).setCellValue(statisticalInfo.getDistributionAmount());
                globalProfits = CalculateUtil.add(globalProfits,statisticalInfo.getTotalProfits());
            }
            totalProfitsRow.createCell(++summaryIndex).setCellValue(globalShouldAmount);
            totalProfitsRow.createCell(++summaryIndex).setCellValue(globalProfits);
        }


        //设置自适应列宽
        for (int i = 0; i < titles.length; i++) {
            //适用中文
            sheet.setColumnWidth(i, titles[i].getBytes().length * 2 * 260);
        }
        return workbook;
    }

    private String[] generateTitles(String startTime, String endTime) throws Exception {

        Date startDate = DateUtils.getDate(startTime,DateUtils.YYYY_MM_DD);
        Date endDate = DateUtils.getDate(endTime,DateUtils.YYYY_MM_DD);
        int days = DateUtils.betweenDays(startDate,endDate);

         return buildTitles(startDate,endDate,days);
    }

    private String[] buildTitles(Date startDate, Date endDate, int days) {
        String[] titles = new String[days + 9];
        titles[0] = "姓  名";
        int index = 1;
        Calendar calendar = Calendar.getInstance();

        while (startDate.getTime() <= endDate.getTime()){
            calendar.setTime(startDate);
            titles[index++]= calendar.get(Calendar.DAY_OF_MONTH)+"号";
            calendar.add(Calendar.DAY_OF_MONTH,1);
            startDate = calendar.getTime();
        }

        titles[index++] = "分配总额";
        titles[index++] = "上班天数";
        titles[index++] = "个人收入";
        titles[index++] = "个人应收";
        titles[index++] = "进";
        titles[index++] = "补";
        titles[index++] = "备  注";
        return titles;
    }

    public static void main(String[] args) throws Exception{
        RangeExcelService rangeExcelService = new RangeExcelService();
        HSSFWorkbook sheets = rangeExcelService.generateRangeExcel("2020-07-11", "2020-07-20", 1);
        ExcelService excelService = new ExcelService();
        excelService.writeFile(sheets,"D:/4156.xls");
    }

}
