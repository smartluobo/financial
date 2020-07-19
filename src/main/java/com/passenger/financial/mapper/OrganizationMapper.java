package com.passenger.financial.mapper;

import com.passenger.financial.entity.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationMapper {

    Organization findById(int id);

    List<Organization> findByParentId(Integer accountingId);

    List<Organization> findAllAccounting();

}
