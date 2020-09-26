package com.passenger.financial.mapper;

import com.passenger.financial.entity.TenDayRecord;

import java.util.List;

public interface TenDayMapper {

    List<TenDayRecord> findAllByOrganizationId(int accountingOrganizationId);
}
