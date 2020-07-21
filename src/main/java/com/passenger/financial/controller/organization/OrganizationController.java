package com.passenger.financial.controller.organization;

import com.passenger.financial.common.ResultInfo;
import com.passenger.financial.entity.Organization;
import com.passenger.financial.service.organization.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/cms/organization")
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @RequestMapping("/findAllOrganization")
    public ResultInfo getInfo(@RequestBody Map<String,String> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String code = params.get("code");
        try {
            ResultInfo resultInfo = ResultInfo.newCmsSuccessResultInfo();
            //统计上班人数，请假人数， 低保人数，值班人数，未上报人数，核算总人数
            List<Organization> organizations = organizationService.findAllAccounting();
            resultInfo.setData(organizations);
            return resultInfo;
        }catch (Exception e){
            log.error("activity list happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
