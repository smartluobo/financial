package com.passenger.financial.entity;

import lombok.Data;

import java.util.Date;

@Data
public class StatisticalRecord {

    /**
     * 主键id
     */
    private int id;

    /**
     * 运营车辆总数,总纯收入/该值计算日均收入
     */
    private String operationVehicleCount;

    /**
     * 请假数量 请假人数
     */
    private String leaveCount;

    /**
     * 总车辆 当前核算组织下运营车辆总数
     */
    private int totalVehicleCount;

    /**
     * 统计日期
     */
    private String statisticalDate;

    /**
     * 总收入
     */
    private String totalIncome;

    /**
     * 总支出
     */
    private String totalSpending;

    /**
     * 总纯收入
     */
    private String totalProfits;

    /**
     * 人均分配
     */
    private String distributionAmount;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新日期
     */
    private Date updateTime;

    /**
     * 记录状态 0 未复核 1已复核
     */
    private int status;

    /**
     * 核算组织id
     */
    private int accountingOrganizationId;
    /**
     * 核算组织名称
     */
    private String accountingOrganizationName;
}
