package com.passenger.financial.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.passenger.financial.annotation.ExcelColumn;
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
     * 司机姓名
     */
    @ExcelColumn(value = "姓名", col = 1)
    private String name;

    /**
     * 司机电话号码
     */
    @ExcelColumn(value = "电话号码", col = 2)
    private String phone;
    /**
     * 直属组织id
     */
    @ExcelColumn(value = "直接组织id", col = 3)
    private int organizationId;
    /**
     * 直属组织名称
     */
    @ExcelColumn(value = "直接组织名称", col = 4)
    private String organizationName;

    /**
     * 核算组织id
     */
    @ExcelColumn(value = "核算组织id", col = 5)
    private int accountingOrganizationId;
    /**
     * 核算组织名称
     */
    @ExcelColumn(value = "核算组织名称", col = 6)
    private String accountingOrganizationName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
