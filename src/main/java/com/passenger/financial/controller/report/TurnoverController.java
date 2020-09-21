package com.passenger.financial.controller.report;

import com.passenger.financial.common.CommonConstant;
import com.passenger.financial.common.ResultInfo;
import com.passenger.financial.entity.ApiUser;
import com.passenger.financial.entity.Driver;
import com.passenger.financial.entity.StatisticalInfo;
import com.passenger.financial.entity.TurnoverRecord;
import com.passenger.financial.service.apiUser.ApiUserService;
import com.passenger.financial.service.driver.DriverService;
import com.passenger.financial.service.report.TurnoverService;
import com.passenger.financial.service.statistical.StatisticalService;
import com.passenger.financial.vo.TurnoverVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/turnover")
public class TurnoverController {

    @Resource
    private TurnoverService turnoverService;

    @Resource
    private DriverService driverService;

    @Resource
    private ApiUserService apiUserService;

    @Resource
    private StatisticalService statisticalService;

    @RequestMapping("/getInfo")
    public ResultInfo getInfo(@RequestBody Map<String,String> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        String currentDate = params.get("currentDate");
        String openId = params.get("openId");
        if (StringUtils.isEmpty(currentDate) || StringUtils.isEmpty(openId)){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
            ApiUser apiUserByOpenId = apiUserService.findApiUserByOpenId(openId);
            if (apiUserByOpenId == null){
                return ResultInfo.newFailResultInfo("用户不存在");
            }
            int driverId = apiUserByOpenId.getDriverId();
            if (driverId == 0){
                return ResultInfo.newFailResultInfo("当前微信号没有绑定司机信息，请联系管理员！");
            }
            Driver driver = driverService.findDriverById(driverId);
            if (driver == null){
                return ResultInfo.newFailResultInfo("司机信息不存在，请联系管理员");
            }

            TurnoverRecord turnoverRecord = turnoverService.findTurnoverRecord(currentDate, driverId);

            TurnoverVo vo = new TurnoverVo();
            vo.setDriver(driver);
            vo.setTurnoverRecord(turnoverRecord);
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            resultInfo.setData(vo);
            return resultInfo;
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


    @RequestMapping("/report")
    public ResultInfo report(@RequestBody TurnoverRecord record){
        if (record == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        log.info("record : {}",record);
        //查询当日是否有统计记录，如果有统计记录不允许再提交数据
        StatisticalInfo statisticalInfo = statisticalService.findStatisticalRecordByDate(record.getReportDate(),record.getAccountingOrganizationId());
        if (statisticalInfo != null){
            return ResultInfo.newFailResultInfo("当日统计记录已生成，请联系管理员处理");
        }
        try {
            String flag = turnoverService.checkRecord(record);
            if (!CommonConstant.SUCCESS.equals(flag)){
                return ResultInfo.newFailResultInfo(flag);
            }

            ApiUser apiUserByOpenId = apiUserService.findApiUserByOpenId(record.getOpenId());
            if (apiUserByOpenId == null){
                return ResultInfo.newFailResultInfo("用户不存在");
            }
            int driverId = apiUserByOpenId.getDriverId();
            if (driverId == 0){
                return ResultInfo.newFailResultInfo("当前微信号没有绑定司机信息，请联系管理员！");
            }
            Driver driver = driverService.findDriverById(driverId);
            if (driver == null){
                return ResultInfo.newFailResultInfo("司机信息不存在，请联系管理员");
            }
            record.setDriverId(driver.getId());
            if (record.getId() == 0){
                TurnoverRecord turnoverRecord = turnoverService.findTurnoverRecord(record.getReportDate(), record.getDriverId());
                if (turnoverRecord != null){
                    return ResultInfo.newFailResultInfo("当日已存在上报记录，请联系管理员");
                }
                record.setCreateTime(new Date());
                String initFlag = turnoverService.initRecord(record,driver);
                if (!CommonConstant.SUCCESS.equals(initFlag)){
                    return ResultInfo.newFailResultInfo(initFlag);
                }
                turnoverService.insert(record);
            }else{
                Map<String, Object> resultMap = turnoverService.initUpdateRecord(record);
                if (!CommonConstant.SUCCESS.equals(String.valueOf(resultMap.get("msg")))){
                    return ResultInfo.newFailResultInfo(String.valueOf(resultMap.get("msg")));
                }
                turnoverService.update((TurnoverRecord)resultMap.get("record"));
            }
            return ResultInfo.newCmsSuccessResultInfo();
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

}
