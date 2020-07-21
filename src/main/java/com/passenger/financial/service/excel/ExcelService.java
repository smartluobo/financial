package com.passenger.financial.service.excel;

import com.passenger.financial.entity.StatisticalInfo;
import com.passenger.financial.entity.TurnoverRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExcelService {

    public HSSFWorkbook generateExcel(StatisticalInfo statisticalInfo, Map<Integer,List<TurnoverRecord>>recordsMap){
        HSSFWorkbook workbook = buildLifeCircleGoodsExcel( statisticalInfo,recordsMap);
        return workbook;
    }

    private HSSFWorkbook buildLifeCircleGoodsExcel(StatisticalInfo statisticalInfo, Map<Integer,List<TurnoverRecord>>recordsMap) {
        String[] title = {"姓    名","直属机构","毛收入", "油  费", "纯收入","类  型", "上班时长", "分配金额"};
        //创建一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFCellStyle headStyle = workbook.createCellStyle();
        sheet.autoSizeColumn(1, true);
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        HSSFFont headFont = workbook.createFont();
        headFont.setFontHeightInPoints((short) 18);//设置字体大小
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        headStyle.setFont(headFont);

        CellStyle directStyle = workbook.createCellStyle();
        directStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
        directStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        directStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        directStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        directStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        directStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        directStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        directStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        directStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        directStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        HSSFFont directFont = workbook.createFont();
        directFont.setColor(IndexedColors.WHITE.getIndex());
        directFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        directStyle.setFont(directFont);

        CellStyle statisticalStyle = workbook.createCellStyle();
        statisticalStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        statisticalStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        statisticalStyle.setFont(directFont);

        CellStyle errorStyle = workbook.createCellStyle();
        errorStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        errorStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        errorStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        errorStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        errorStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        errorStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        errorStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        errorStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        HSSFFont redFont = workbook.createFont();
        redFont.setColor(IndexedColors.RED.getIndex());
        errorStyle.setFont(redFont);

        CellStyle normalStyle = workbook.createCellStyle();
        normalStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        normalStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        normalStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        normalStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        normalStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        normalStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        normalStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        normalStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        HSSFFont blackFont = workbook.createFont();
        blackFont.setColor(IndexedColors.BLACK.getIndex());
        normalStyle.setFont(blackFont);


        CellStyle redStyle = workbook.createCellStyle();
        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        //合并第一行显示
        CellRangeAddress statisticalTile = new CellRangeAddress(0, 0, 0, 7);
        sheet.addMergedRegion(statisticalTile);
        HSSFRow row = sheet.createRow(0);

        HSSFCell supperRow = row.createCell(0);
        supperRow.setCellValue(statisticalInfo.getStatisticalDate()+statisticalInfo.getOrganizationName()+"日均收入统计表");
        supperRow.setCellStyle(headStyle);
        //创建表头
        Row headRow = sheet.createRow(1);
        for (int i = 0; i < title.length; i++) {
            headRow.createCell(i).setCellValue(title[i]);

        }
        int rowIndex = 2;
        if (CollectionUtils.isEmpty(statisticalInfo.getDirectOrganizationInfos())){
            //表示该机构下没有子核算机构
            rowIndex = buildOrganizationDetailSheet(sheet, rowIndex,recordsMap.get(statisticalInfo.getOrganizationId()),errorStyle,normalStyle);
        }else{
            for (StatisticalInfo directOrganizationInfo : statisticalInfo.getDirectOrganizationInfos()) {
                List<TurnoverRecord> records = recordsMap.get(directOrganizationInfo.getOrganizationId());
                rowIndex = buildOrganizationDetailSheet(sheet, rowIndex,records,errorStyle,normalStyle);
                //构建当前机构的统计信息
                rowIndex = buildOrganizationStatisticalSheet(directOrganizationInfo,sheet, directStyle, rowIndex);
            }
        }
        rowIndex = buildOrganizationStatisticalSheet(statisticalInfo,sheet, statisticalStyle, rowIndex);

        //设置自适应列宽
        for (int i = 0; i < title.length; i++) {
            //适用中文
            sheet.setColumnWidth(i, title[i].getBytes().length * 2 * 260);
        }
        return workbook;
    }

    private int buildOrganizationStatisticalSheet(StatisticalInfo organization,HSSFSheet sheet, CellStyle directStyle, int rowIndex) {
        HSSFRow row1 = sheet.createRow(rowIndex);
        rowIndex++;
        HSSFCell d01 = row1.createCell(0);
        d01.setCellValue(organization.getOrganizationName()+"总收入");
        d01.setCellStyle(directStyle);
        HSSFCell d02 = row1.createCell(1);
        d02.setCellValue(Double.parseDouble(organization.getTotalIncome()));
        d02.setCellStyle(directStyle);
        HSSFCell d03 = row1.createCell(2);
        d03.setCellValue(organization.getOrganizationName()+"油费");
        d03.setCellStyle(directStyle);
        HSSFCell d04 = row1.createCell(3);
        d04.setCellValue(Double.parseDouble(organization.getTotalSpending()));
        d04.setCellStyle(directStyle);
        HSSFCell d05 = row1.createCell(4);
        d05.setCellValue(organization.getOrganizationName()+"纯收入");
        d05.setCellStyle(directStyle);
        HSSFCell d06 = row1.createCell(5);
        d06.setCellValue(Double.parseDouble(organization.getTotalProfits()));
        d06.setCellStyle(directStyle);
        HSSFCell d07 = row1.createCell(6);
        d07.setCellValue(organization.getOrganizationName()+"请假人数");
        d07.setCellStyle(directStyle);
        HSSFCell d08 = row1.createCell(7);
        d08.setCellValue(organization.getLeaveCount());
        d08.setCellStyle(directStyle);
        HSSFRow row2 = sheet.createRow(rowIndex);
        rowIndex++;
        HSSFCell d11 = row2.createCell(0);
        d11.setCellValue(organization.getOrganizationName()+"值班人数");
        d11.setCellStyle(directStyle);
        HSSFCell d12 = row2.createCell(1);
        d12.setCellValue(organization.getDutyCount());
        d12.setCellStyle(directStyle);
        HSSFCell d13 = row2.createCell(2);
        d13.setCellValue(organization.getOrganizationName()+"低保人数");
        d13.setCellStyle(directStyle);
        HSSFCell d14 = row2.createCell(3);
        d14.setCellValue(organization.getLowCount());
        d14.setCellStyle(directStyle);
        HSSFCell d15 = row2.createCell(4);
        d15.setCellValue(organization.getOrganizationName()+"总运营车辆数");
        d15.setCellStyle(directStyle);
        HSSFCell d16 = row2.createCell(5);
        d16.setCellValue(Double.parseDouble(organization.getOperationCount()));
        d16.setCellStyle(directStyle);
        HSSFCell d17 = row2.createCell(6);
        d17.setCellValue(organization.getOrganizationName()+"日均分配");
        d17.setCellStyle(directStyle);
        HSSFCell d18 = row2.createCell(7);
        d18.setCellValue(Double.parseDouble(organization.getDistributionAmount()));
        d18.setCellStyle(directStyle);
        rowIndex++;
        return rowIndex;
    }

    private int buildOrganizationDetailSheet(HSSFSheet sheet, int rowIndex, List<TurnoverRecord> records, CellStyle errorStyle, CellStyle normalStyle) {

        if (CollectionUtils.isEmpty(records)){
            return rowIndex;
        }
        for (TurnoverRecord record : records) {
            Row dataRow = sheet.createRow(rowIndex);
            Cell cell0 = dataRow.createCell(0);
            cell0.setCellStyle(normalStyle);
            cell0.setCellValue(record.getDriverName());//姓名
            Cell cell1 = dataRow.createCell(1);
            cell1.setCellStyle(normalStyle);
            cell1.setCellValue(record.getOrganizationName());//直属机构

            Cell cell2 = dataRow.createCell(2);
            cell2.setCellStyle(normalStyle);
            cell2.setCellValue(Double.parseDouble(StringUtils.isEmpty(record.getIncome())? "0" : record.getIncome()));//毛收入
            Cell cell3 = dataRow.createCell(3);
            cell3.setCellStyle(normalStyle);
            cell3.setCellValue(Double.parseDouble(StringUtils.isEmpty(record.getSpending())? "0" : record.getSpending()));//y邮费
            Cell cell4 = dataRow.createCell(4);
            cell4.setCellStyle(normalStyle);
            cell4.setCellValue(Double.parseDouble(StringUtils.isEmpty(record.getProfits())? "0" : record.getProfits()));//纯收入
            String typeStr = "上班";
            int type = record.getType();
            if (type == 2){
                typeStr="值班";
            }else if (type == 3){
                typeStr = "请假";
            }else if (type == 4){
                typeStr="低保";
            }


            if ("请假".equals(typeStr)){
                Cell cell5 = dataRow.createCell(5);
                cell5.setCellStyle(errorStyle);
                cell5.setCellValue(typeStr);//类型
                Cell cell6 = dataRow.createCell(6);
                cell6.setCellStyle(errorStyle);
                cell6.setCellValue(Double.parseDouble("0"));//上班时长
                Cell cell7 = dataRow.createCell(7);
                cell7.setCellStyle(errorStyle);
                cell7.setCellValue(Double.parseDouble("0"));//日均分配
            }else{
                Cell cell5 = dataRow.createCell(5);
                cell5.setCellStyle(normalStyle);
                cell5.setCellValue(typeStr);//类型
                Cell cell6 = dataRow.createCell(6);
                cell6.setCellStyle(normalStyle);
                cell6.setCellValue(Double.parseDouble(StringUtils.isEmpty(record.getWorkTimeValue())? "0" : record.getWorkTimeValue()));//上班时长
                Cell cell7 = dataRow.createCell(7);
                cell7.setCellStyle(normalStyle);
                cell7.setCellValue(Double.parseDouble(StringUtils.isEmpty(record.getDistributionAmount())? "0" : record.getDistributionAmount()));//日均分配
            }

            rowIndex++;
        }
        return rowIndex;
    }

    public void writeFile(HSSFWorkbook workbook, String filePath){

        FileOutputStream stream = null;
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            //将workbook写入到输出流
            stream = new FileOutputStream(file);
            //写文件
            workbook.write(stream);
        }catch (Exception e){

        }finally {
            //关闭流
            if (stream != null){
                try {
                    stream.flush();
                    stream.close();
                }catch (Exception e){

                }
            }

        }


    }

    public static void main(String[] args) {
        ExcelService excelService = new ExcelService();
        StatisticalInfo statisticalInfo = new StatisticalInfo();
        TurnoverRecord record = new TurnoverRecord();
        record.setId(1);
        record.setDriverId(12);
        record.setDriverName("zhangsan");
        record.setPhone("13530852671");
        record.setIncome("288.8");
        record.setSpending("88");
        record.setProfits("200");
        record.setWorkTimeValue("0.5");
        record.setOrganizationId(2);
        record.setOrganizationName("旧城联营");
        record.setType(2);
        record.setDistributionAmount("182.88");

        List<TurnoverRecord> records = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            records.add(record);
        }
        TurnoverRecord record1 = new TurnoverRecord();
        record1.setId(1);
        record1.setDriverId(12);
        record1.setDriverName("zhangsan");
        record1.setPhone("13530852671");
        record1.setIncome("288.8");
        record1.setSpending("88");
        record1.setProfits("200");
        record1.setWorkTimeValue("0.5");
        record1.setOrganizationId(3);
        record1.setOrganizationName("中心联营");
        record1.setType(2);
        record1.setDistributionAmount("182.88");

        List<TurnoverRecord> records1 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            records1.add(record1);
        }
        statisticalInfo.setTotalCount(50);
        statisticalInfo.setLeaveCount(2);
        statisticalInfo.setDutyCount(3);
        statisticalInfo.setLowCount(2);
        statisticalInfo.setOperationCount("28.5");
        statisticalInfo.setTotalIncome("28886");
        statisticalInfo.setTotalSpending("256.86");
        statisticalInfo.setTotalProfits("25560");
        statisticalInfo.setOrganizationId(1);
        statisticalInfo.setOrganizationName("旧中联营");
        statisticalInfo.setDistributionAmount("188.56");
        statisticalInfo.setStatisticalDate("2020-07-07");
        StatisticalInfo statisticalInfo1 = new StatisticalInfo();
        StatisticalInfo statisticalInfo2 = new StatisticalInfo();
        BeanUtils.copyProperties(statisticalInfo,statisticalInfo1);
        statisticalInfo1.setOrganizationId(2);
        statisticalInfo1.setOrganizationName("旧城联营");
        BeanUtils.copyProperties(statisticalInfo,statisticalInfo2);
        statisticalInfo2.setOrganizationId(3);
        statisticalInfo2.setOrganizationName("中心联营");
        List<StatisticalInfo> directInfos = new ArrayList<>();
        directInfos.add(statisticalInfo1);
        directInfos.add(statisticalInfo2);
        statisticalInfo.setDirectOrganizationInfos(directInfos);

        Map<Integer,List<TurnoverRecord>> recordsMap = new HashMap<>();
        recordsMap.put(2,records);
        recordsMap.put(3,records1);
        HSSFWorkbook sheets = excelService.generateExcel(statisticalInfo, recordsMap);
        excelService.writeFile(sheets,"D:/my_code/financial/123.xls");

    }


}
