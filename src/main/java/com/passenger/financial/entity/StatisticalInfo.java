package com.passenger.financial.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StatisticalInfo {

    /**
     * 父级核算机构统计记录id
     */
    private int parentId;

    private int id;

    /**
     * 总司机数
     */
    private int totalCount;

    /**
     * 请假人数
     */
    private int leaveCount ;

    /**
     * 值班人数
     */
    private int dutyCount;

    /**
     * 低保人数
     */
    private int lowCount;

    /**
     * 未提交信息人数
     */
    private int noCommitCount;

    /**
     * 运营人数
     */
    private String operationCount = "0";

    /**
     * 总收入
     */
    private String totalIncome = "0";

    /**
     * 总支出
     */
    private String totalSpending = "0";

    /**
     * 总纯收入
     */
    private String totalProfits = "0" ;

    /**
     * 未提交司机信息
     */
    private List<Driver> noCommitDriverInfos;

    /**
     * 直属组织相关数据
     */
    private List<StatisticalInfo> directOrganizationInfos;

    /**
     * 组织id
     */
    private int organizationId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 分配金额
     */
    private String distributionAmount = "0";

    /**
     * 统计日期
     */
    private String statisticalDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 统计文件路径
     */
    private String filePath;

    private int source;

    private String downloadUrl;
}
