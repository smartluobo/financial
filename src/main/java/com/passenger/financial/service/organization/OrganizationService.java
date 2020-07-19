package com.passenger.financial.service.organization;

import com.passenger.financial.entity.Organization;
import com.passenger.financial.mapper.OrganizationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class OrganizationService {

    @Resource
    private OrganizationMapper organizationMapper;


    public List<Organization> findAllAccounting() {
        return organizationMapper.findAllAccounting();
    }
}
