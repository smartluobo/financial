package com.passenger.financial.service.apiUser;

import com.passenger.financial.entity.ApiUser;
import com.passenger.financial.entity.Driver;
import com.passenger.financial.mapper.ApiUserMapper;
import com.passenger.financial.mapper.DriverMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ApiUserService {

    @Resource
    private ApiUserMapper apiUserMapper;

    @Resource
    private DriverMapper driverMapper;

    public ApiUser findApiUserByOpenId(String openId){
        return apiUserMapper.selectByOpenId(openId);
    }

    /**
     * 保持用户信息
     * @param oppenId
     */
    public void saveApiUser (String oppenId) {
        ApiUser record = new ApiUser();
        record.setOpenId(oppenId);

        ApiUser apiUser = apiUserMapper.selectByOpenId(oppenId);
        if (null == apiUser) {
            apiUserMapper.insert(record);
        }
    }

    public void updateApiUserInfo (ApiUser record) {
        apiUserMapper.updateByPrimaryKey(record);
    }

    public void initDriver() {
        List<ApiUser> apiUsers = apiUserMapper.findNoInitDriverInfoUser();
        if (!CollectionUtils.isEmpty(apiUsers)){
            for (ApiUser apiUser : apiUsers) {
                Driver driverInfo = driverMapper.findById(apiUser.getDriverId());
                apiUser.setDriverName(driverInfo.getName());
                apiUser.setOrganizationId(driverInfo.getOrganizationId());
                apiUser.setOrganizationName(driverInfo.getOrganizationName());
                apiUser.setAccountingOrganizationId(driverInfo.getAccountingOrganizationId());
                apiUser.setAccountingOrganizationName(driverInfo.getAccountingOrganizationName());
                apiUser.setUpdateTime(new Date());
                apiUserMapper.updateDriverInfoById(apiUser);
            }
        }
    }
}
