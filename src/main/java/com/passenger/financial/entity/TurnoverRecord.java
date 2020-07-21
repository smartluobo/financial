package com.passenger.financial.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TurnoverRecord {

    /**
     * 主键id
     */
    private int id;

    /**
     * 司机id
     */
    private int driverId;

    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 司机电话
     */
    private String phone;

    /**
     * 毛收入
     */
    private String income = "0";

    /**
     * 开支
     */
    private String spending = "0";

    /**
     * 纯收入
     */
    private String profits = "0";

    /**
     * 工作时长 1 工作时长1天 ---2 工作半天
     */
    private int workTimeType;

    private String workTimeValue;

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

    /**
     * 日均分配金额
     */
    private String distributionAmount = "0";

    /**
     * 应该收入金额
     */
    private String shouldAmount = "0";

    /**
     * 类型 1-上班 2-值班 3-请假 4-低保
     */
    private int type;

    /**
     * 统计状态 0 未统计 1 已统计
     */
    private int statisticalType;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 上报日期
     */
    private String reportDate;

    /**
     * 微信open_id
     */
    private String openId;

    /**
     * 每日进金额 实收金额小于日营收金额 该值大于0
     */
    private String intoAmount;

    /**
     *  每日补金额 实收金额大于营收金额该值大于0
     */
    private String outAmount;
}
