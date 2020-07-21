package com.passenger.financial.service.driver;

import com.passenger.financial.entity.Driver;
import com.passenger.financial.mapper.DriverMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DriverService {

    @Resource
    private DriverMapper driverMapper;
    public Driver findDriverByPhone(String phone){

        return driverMapper.findDriverByPhone(phone);

    }

    public List<Driver> findDriverByCondition(Map<String, String> params) {
        return driverMapper.findDriverByCondition(params);
    }

    public void batchSaveDriver(List<Driver> drivers) {
        Date date = new Date();
        for (Driver driver : drivers) {
            driver.setCreateTime(date);
            driver.setUpdateTime(date);
            driverMapper.insert(driver);
        }
    }
}
