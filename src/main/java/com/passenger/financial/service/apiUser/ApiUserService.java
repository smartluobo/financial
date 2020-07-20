package com.passenger.financial.service.apiUser;

import com.passenger.financial.entity.ApiUser;
import com.passenger.financial.mapper.ApiUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiUserService {

    @Resource
    private ApiUserMapper apiUserMapper;

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

}
