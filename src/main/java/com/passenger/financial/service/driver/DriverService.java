package com.passenger.financial.service.driver;

import com.passenger.financial.entity.Driver;
import com.passenger.financial.mapper.DriverMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class DriverService {

    @Resource
    private DriverMapper driverMapper;
    public Driver findDriverByPhone(String phone){

        return driverMapper.findDriverByPhone(phone);

    }
}
