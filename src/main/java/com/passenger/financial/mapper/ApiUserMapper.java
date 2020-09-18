package com.passenger.financial.mapper;

import com.passenger.financial.entity.ApiUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApiUser record);

    ApiUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ApiUser record);

    ApiUser selectByOpenId(String openId);

    void updateDriverInfoById(ApiUser apiUser);

    List<ApiUser> findNoInitDriverInfoUser();

    ApiUser findApiUserByPhone(String phone);

}
