package com.passenger.financial.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ApiUser {
    private int id;

    private String nickName;

    private String wechatNum;

    private String openId;

    private int driverId;

    /**
     * 电话号码
     */
    private String phoneNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private String userHeadImage;

    /**
     * 司机姓名
     */
    private String driverName;

    /**
     * 直接组织id
     */
    private int organizationId;

    /**
     * 直接组织名称
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

}
