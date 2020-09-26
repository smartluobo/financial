package com.passenger.financial.entity;

import lombok.Data;

@Data
public class TenDayRecord {

    private int id;

    private String accountOrganizationName;

    private String distributionAmount;

    private String startTime;

    private String endTime;

    private int accountOrganizationId;
}
