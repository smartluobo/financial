package com.passenger.financial.mapper;

import com.passenger.financial.entity.Driver;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverMapper {
    Driver findDriverByPhone(String phone);

    Driver findById(int driverId);

    int findByAccountIdCount(@Param("accountingId") Integer accountingId);

    List<Driver> findNoCommitInfo(@Param("accountingId") Integer accountingId, @Param("currentDate") String currentDate);
}
