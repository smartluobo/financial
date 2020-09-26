package com.passenger.financial.controller.statistical;


import com.passenger.financial.common.CommonConstant;
import com.passenger.financial.common.ResultInfo;
import com.passenger.financial.entity.ApiUser;
import com.passenger.financial.entity.TenDayRecord;
import com.passenger.financial.service.apiUser.ApiUserService;
import com.passenger.financial.service.statistical.TenDayService;
import com.passenger.financial.utils.CalculateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/tenDay")
public class ApiStatisticalController {

    @Resource
    private ApiUserService apiUserService;

    @Resource
    private TenDayService tenDayService;

    @RequestMapping("/findAllByOrganizationId")
    public ResultInfo findAllByOrganizationId(String openId){
        try {
            if (StringUtils.isEmpty(openId)){
                return ResultInfo.newFailResultInfo("当前用户未登录，请先登录");
            }
            ApiUser apiUser = apiUserService.findApiUserByOpenId(openId);
            if (apiUser == null){
                return ResultInfo.newFailResultInfo("当期用户信息不存在");
            }
            int accountingOrganizationId = apiUser.getAccountingOrganizationId();
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            //统计上班人数，请假人数， 低保人数，值班人数，未上报人数，核算总人数
            List<TenDayRecord> records = tenDayService.findAllByOrganizationId(accountingOrganizationId);
            String totalAmount = "0";
            for (TenDayRecord record : records) {
                totalAmount = CalculateUtil.add(totalAmount,record.getDistributionAmount());
            }
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("records",records);
            resultMap.put("totalAmount",totalAmount);
            resultInfo.setData(resultMap);
            return resultInfo;
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
