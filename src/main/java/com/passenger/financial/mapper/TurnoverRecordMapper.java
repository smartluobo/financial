package com.passenger.financial.mapper;

import com.passenger.financial.entity.TurnoverRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnoverRecordMapper {

    TurnoverRecord findRecordByDateAndDriverId(@Param("currentDate") String currentDate, @Param("driverId") int driverId);

    void insert(TurnoverRecord record);

    void update(TurnoverRecord record);

    List<TurnoverRecord> findAllRecordByDateAndAccountId(@Param("currentDate") String currentDate, @Param("accountingId") Integer accountingId);

    TurnoverRecord findById(int id);

    void cancelStatisticalUpdate(@Param("reportDate") String statisticalDate, @Param("organizationId") int organizationId);

    List<TurnoverRecord> findRecordByDriverId(Integer driverId);
}
