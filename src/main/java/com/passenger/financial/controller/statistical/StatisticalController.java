package com.passenger.financial.controller.statistical;


import com.passenger.financial.common.CommonConstant;
import com.passenger.financial.common.ResultInfo;
import com.passenger.financial.entity.StatisticalInfo;
import com.passenger.financial.service.statistical.StatisticalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/cms/statistical")
public class StatisticalController {

    @Resource
    private StatisticalService statisticalService;

    @RequestMapping("/getStatisticalPreInfo")
    public ResultInfo getInfo(@RequestBody Map<String,String> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        String currentDate = params.get("currentDate");
        String accountingOrganizationId = params.get("accountingOrganizationId");
        if (StringUtils.isEmpty(currentDate) || StringUtils.isEmpty(accountingOrganizationId)){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            //统计上班人数，请假人数， 低保人数，值班人数，未上报人数，核算总人数
            //查询当日是否有统计记录
            StatisticalInfo statisticalRecord = statisticalService.findStatisticalRecordByDate(currentDate, Integer.valueOf(accountingOrganizationId));
            if (statisticalRecord != null){
                List<StatisticalInfo> directRecords =  statisticalService.findStatisticalDetailByParentId(statisticalRecord.getId());
                if (!CollectionUtils.isEmpty(directRecords)){
                    statisticalRecord.setDirectOrganizationInfos(directRecords);
                }
                statisticalRecord.setSource(1);
                resultInfo.setData(statisticalRecord);
                return resultInfo;
            }
            StatisticalInfo statisticalInfo = statisticalService.getStatisticalPreInfo(currentDate,accountingOrganizationId);
            resultInfo.setData(statisticalInfo);
            return resultInfo;
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/statistical")
    public ResultInfo statistical(String currentDate, String accountingOrganizationId){
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            //统计上班人数，请假人数， 低保人数，值班人数，未上报人数，核算总人数
            Map<String,String> resultMap = statisticalService.statistical(currentDate,accountingOrganizationId);
            if (CommonConstant.SUCCESS.equals(resultMap.get("msg"))){
                return resultInfo;
            }else {
                return ResultInfo.newFailResultInfo(resultMap.get("msg"));
            }
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
    @RequestMapping("/download")
    public ResultInfo download(String currentDate, String accountingOrganizationId,HttpServletResponse response){
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            String filename = statisticalService.download(currentDate, accountingOrganizationId);
            if (filename == null){
                return ResultInfo.newFailResultInfo(currentDate+"无统计记录，请先统计");
            }
            String fileRealName = currentDate+"日统计表.xls";
            OutputStream out = null;
            try {
                response.addHeader("content-disposition", "attachment;filename="
                        + java.net.URLEncoder.encode(fileRealName, "utf-8"));
                // 2.下载
                out = response.getOutputStream();
                // inputStream：读文件，前提是这个文件必须存在，要不就会报错
                InputStream is = new FileInputStream(filename);
                byte[] b = new byte[4096];
                int size = is.read(b);
                while (size > 0) {
                    out.write(b, 0, size);
                    size = is.read(b);
                }
                out.close();
                is.close();
            } catch (Exception e) {
                log.error("download excel happen exception",e);
            }
            return resultInfo;
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/cancel")
    public ResultInfo cancel(String currentDate, String accountingOrganizationId){
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            StatisticalInfo statisticalRecordByDate = statisticalService.findStatisticalRecordByDate(currentDate, Integer.valueOf(accountingOrganizationId));
            if (statisticalRecordByDate == null){
                return ResultInfo.newFailResultInfo("当日无统计记录，取消失败");
            }
            statisticalService.cancel(statisticalRecordByDate);
            return resultInfo;
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/rangeStatistical")
    public ResultInfo rangeStatistical(String startDate, String endDate,int accountingOrganizationId){
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            statisticalService.rangeStatistical(startDate,endDate,accountingOrganizationId);
            return resultInfo;
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
