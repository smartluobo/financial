package com.passenger.financial.service.statistical;

import com.passenger.financial.entity.TenDayRecord;
import com.passenger.financial.mapper.TenDayMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class TenDayService {

    @Resource
    private TenDayMapper tenDayMapper;

    public List<TenDayRecord> findAllByOrganizationId(int accountingOrganizationId) {
        return tenDayMapper.findAllByOrganizationId(accountingOrganizationId);
    }
}
