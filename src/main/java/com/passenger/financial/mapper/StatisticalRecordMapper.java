package com.passenger.financial.mapper;

import com.passenger.financial.entity.StatisticalInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticalRecordMapper {
    void insertStatistical(StatisticalInfo statisticalPreInfo);

    void insertDetailStatistical(StatisticalInfo directOrganizationInfo);

    StatisticalInfo findStatisticalRecordByDate(@Param("statisticalDate") String statisticalDate, @Param("accountingOrganizationId") int accountingOrganizationId);

    void deleteById(int id);

    void deleteDetailByParentId(int id);

    List<StatisticalInfo> findStatisticalRecordByRange(@Param("startDate") String startTime, @Param("endDate") String endTime, @Param("organizationId") int organizationId);

    List<StatisticalInfo> findStatisticalDetailByParentId(int parentId);
}
