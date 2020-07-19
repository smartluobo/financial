package com.passenger.financial.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Organization {

    /**
     * 主键id
     */
    private int id;

    /**
     * 是否是核算顶级组织  0 不是，1是
     */
    private int isParent;

    /**
     * 顶级核算组织id
     */
    private int parentId;

    /**
     * 核算组织名称
     */
    private String organizationName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
