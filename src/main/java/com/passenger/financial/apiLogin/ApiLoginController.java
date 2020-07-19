package com.passenger.financial.apiLogin;

import com.passenger.financial.common.ResultInfo;
import com.passenger.financial.entity.ApiUser;
import com.passenger.financial.service.apiLogin.ApiLoginService;
import com.passenger.financial.service.apiUser.ApiUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/login")
public class ApiLoginController {

    @Resource
    private ApiLoginService apiLoginService;

    @Resource
    private ApiUserService apiUserService;

    @RequestMapping("/login")
    public ResultInfo login(@RequestBody Map<String,String> codeParam){
        log.info("api user login call.....");
        String code = null;
        try {
            if (CollectionUtils.isEmpty(codeParam)){
                return ResultInfo.newEmptyResultInfo();
            }
            code = codeParam.get("code");
            if (StringUtils.isEmpty(code)){
                return ResultInfo.newEmptyResultInfo();
            }
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            String oppenId = apiLoginService.login(code);
            log.error("***************oppenId : {}",oppenId);
            if (!StringUtils.isEmpty(oppenId)){
                apiUserService.saveApiUser(oppenId);
                resultInfo.setData(oppenId);
                return resultInfo;
            }
            return ResultInfo.newFailResultInfo();
        }catch (Exception e){
            log.error("login happen exception code : {}",code,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/reportApiUserInfo")
    public ResultInfo reportApiUserInfo(@RequestBody Map<String,String> params){

        if (params == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String nickName = params.get("nickName");
        String userHeadImage = params.get("userHeadImage");
        log.info("reportApiUserInfo current user oppenId : {}, nickName: {},userHeadImage : {}",oppenId,nickName,userHeadImage);
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            ApiUser apiuser = apiUserService.findApiUserByOpenId(oppenId);
            if (apiuser == null){
                return ResultInfo.newFailResultInfo("用户不存在，请先授权登陆");
            }
            apiuser.setNickName(nickName);
            apiuser.setUserHeadImage(userHeadImage);
            apiUserService.updateApiUserInfo(apiuser);
            return resultInfo;
        }catch (Exception e){
            log.error("calculateGoodsOrderPrice GoodsOrderParamVo : {}",params,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
