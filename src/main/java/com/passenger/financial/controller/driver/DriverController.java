package com.passenger.financial.controller.driver;

import com.passenger.financial.common.ResultInfo;
import com.passenger.financial.entity.Driver;
import com.passenger.financial.entity.Organization;
import com.passenger.financial.service.driver.DriverService;
import com.passenger.financial.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/cms/driver")
public class DriverController {

    @Resource
    private DriverService driverService;

    @RequestMapping("/list")
    public ResultInfo getInfo(@RequestBody Map<String,String> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            List<Driver> drivers = driverService.findDriverByCondition(params);
            resultInfo.setData(drivers);
            return resultInfo;
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


    @PostMapping("/import")
    public ResultInfo importDriver(@RequestParam(value="uploadFile", required = false) MultipartFile file){
        if (file == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            //统计上班人数，请假人数， 低保人数，值班人数，未上报人数，核算总人数
            List<Driver> drivers = ExcelUtils.readExcel("", Driver.class, file);
            driverService.batchSaveDriver(drivers);
            return resultInfo;
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
