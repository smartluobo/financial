package com.passenger.financial.mapper;

import com.passenger.financial.entity.ApiUser;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApiUser record);

    ApiUser selectByPrimaryKey(Integer id);

    int updateOpenIdSelective(ApiUser record);

    int updateByPrimaryKey(ApiUser record);

    ApiUser selectByOpenId(String openId);
}
