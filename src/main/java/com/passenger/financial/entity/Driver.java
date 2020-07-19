package com.passenger.financial.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Driver {

    /**
     * 主键id
     */
    private int id;
    /**
     * 司机电话号码
     */
    private String phone;
    /**
     * 司机姓名
     */
    private String name;
    /**
     * 直属组织id
     */
    private int organizationId;
    /**
     * 直属组织名称
     */
    private String organizationName;

    /**
     * 核算组织id
     */
    private int accountingOrganizationId;
    /**
     * 核算组织名称
     */
    private String accountingOrganizationName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
