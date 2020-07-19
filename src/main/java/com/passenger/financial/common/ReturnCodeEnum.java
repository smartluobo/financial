package com.passenger.financial.common;

/**
 * 业务返回码枚举类

 */
public enum ReturnCodeEnum {
    UNDEFINED(0, "UNDEFINED"),
    //-------------------服务相关的状态码-------------------
    /** 请求数据成功 */
    REQUEST_SUCCESS(1000, "OK"),
    /** 请求数据异常 */
    REQUEST_EXCEPTION(100001, "SYSTEM EXCEPTION"),
    /** 参数为空 */
    PARAMETER_EMPTY(100002, "参数为空"),
    /** 参数格式错误 */
    PARAMETER_TYPE_ERROR(100003, "参数类型错误"),
    /** 登出失败 */
    LOGOUT_ERROR(100004, "LOGOUT_ERROR"),
    /** 登录失败 */
    LOGIN_ERROR(100005, "LOGIN_ERROR"),
    /** 登录成功 */
    LOGIN_SUCCESS(100006, "LOGIN_SUCCESS"),
    /** 权限不足 */
    NO_PERMISSION(100007, "NO_PERMISSION"),
    /** session过期 */
    SESSION_EXPIRE(100008, "会话超时，请重新登录"),
    /** 用户名或密码错误 **/
    LOGIN_INFO_ERROR(100009, "LOGIN_INFO_ERROR"),
    /** 验证码错误 **/
    CAPTCHA_ERROR(100010, "CAPTCHA_ERROR"),
    /** 文件上传错误 **/
    UPLOAD_FILE_ERROR(100011, "UPLOAD_FILE_ERROR"),
    /** 数据已存在 */
    DATA_EXIST_ERROR(100012, "数据已存在"),
    /*** 数据不存在*/
    DATA_NOT_EXIST_ERROR(100013, "数据不存在"),
    /** 业务异常 **/
    BUSINESS_EXCEPTION(100014, "BUSINESS EXCEPTION"),
    /** 超过错误次数需要验证码登录 **/
    LOGIN_ERROR_BY_COUNT(100015, "LOGIN_ERROR_BY_COUNT"),
    /** 旧的密码错误 **/
    OLD_PASSWORD_IS_ERROR(100016, "OLD_PASSWORD_IS_ERROR"),


    /** 后台程序错误 */
    PROGRAM_ERROR(999999, "PROGRAM ERROR"),




    //-------------------业务相关的状态码-------------------
    USER_NOT_EXITS(200000, "user is not exits"),
    USER_EMAIL_NOT_MATCH(200001, "username is not match email"),
    ROLE_HAS_RELATION_WITH_USER(200002, "role has relation with user"),
    USER_NAME_IS_EXITS(200003, "user name is exits"),
    ROOT_MENU_CAN_NOT_DELETE(20004, "root menu can not delete"),
    ROLE_NOT_EXITS(20005, "role is not exits"),
    ROLE_NAME_IS_EXITS(20006, "role name is exits"),
    AREA_DELETE_ERROR_ROOT(20007, "root area cannot be deleted"),

    UPLOAD_FILE_NAME_ERROR(20008, "上传失败，文件名不符合规则"),
    UPLOAD_FILE_SIZE_ERROR(20009, "上传失败，文件大小不符合"),
    TAB_IS_NOT_IN_MACHINE_TAB(20009, "上传失败，TAB不存在于任何一个机型TAB中"),
    WATERFALL_LAYOUT_IS_NOT_EXITS(20010, "瀑布流布局信息不存在！"),
    SAVE_WATERFALL_LAYOUT_SCHEDULE_ERROR(20011,"保存排期失败：此瀑布流布局已经排期"),
    PUBLISH_WATERFALL_LAYOUT_SCHEDULE_ERROR(20012,"发布排期失败：此瀑布流布局排期状态不对"),
    WATERFALL_LAYOUT_SCHEDULE_IS_NOT_EXITS(20013, "瀑布流布局排期信息不存在！"),
    WATERFALL_LAYOUT_RESOURCE_IS_NOT_EXITS(20014, "瀑布流资源信息不存在！"),
    SAVE_SCHEDULE_FAIL_THE_TEMPLATE_SCHEDULED(20015, "保存排期失败：此模板已经排期！"),
    SAVE_SCHEDULE_FAIL_BLOCK_RES_EMPTY(20016, "排期失败：有资源为空的Block，请添加资源，TAB名称为："),
    SAVE_SCHEDULE_FAIL_FOR_MEM_LIMIT(20017, "排期失败,模板大小超出该机型内存限制："),
    SCHEDULE_LOCATION_CONFLICT(20018, "排期位置信息冲突"),
    PICTURE_EXCEED_MAX_SIZE(20019, "选定资源中存在图片超过最大尺寸限制"),
    WATERFALL_LAYOUT_SCHEDULE_TIME_IS_ERROR(20020, "瀑布流发布排期时间！");



    private Integer status;
    private String msg;

    ReturnCodeEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus(int status) {
        for (ReturnCodeEnum r : ReturnCodeEnum.values()) {
            if (r.getStatus() == status) {
                return status;
            }
        }
        return -1;
    }

    public String getMsg(int status) {
        for (ReturnCodeEnum r : ReturnCodeEnum.values()) {
            if (r.getStatus() == status) {
                return r.getMsg();
            }
        }
        return "undefined";
    }

    @Override
    public String toString() {
        return "{\"status\":" + status + ",\"msg\":\"" + msg + "\"}";
    }}
