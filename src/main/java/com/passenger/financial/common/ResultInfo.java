package com.passenger.financial.common;


public class ResultInfo {
    private int code;
    private String msg;
    private Object data;
    private long total;

    public ResultInfo(ReturnCodeEnum returnCodeEnum){
        code = returnCodeEnum.getStatus();
        msg = returnCodeEnum.getMsg();
    }

    public ResultInfo(){
        code = ReturnCodeEnum.REQUEST_SUCCESS.getStatus();
        msg = ReturnCodeEnum.REQUEST_SUCCESS.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public static ResultInfo newExceptionResultInfo(){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(500);
        resultInfo.setMsg("programming exception");
        return resultInfo;
    }

    public static ResultInfo newSuccessResultInfo(){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(200);
        resultInfo.setMsg("success");
        return resultInfo;
    }

    public static ResultInfo newCmsSuccessResultInfo(){
        return new ResultInfo(ReturnCodeEnum.REQUEST_SUCCESS);
    }

    public static ResultInfo newEmptyResultInfo(){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(300);
        resultInfo.setMsg("result is null");
        return resultInfo;
    }

    public static ResultInfo newEmptyParamsResultInfo(){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(303);
        resultInfo.setMsg("params is null");
        return resultInfo;
    }

    public static ResultInfo newParameterErrorResultInfo(){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(400);
        resultInfo.setMsg("result is null");
        return resultInfo;
    }

    public static ResultInfo newNoLoginResultInfo() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(601);
        resultInfo.setMsg("no login");
        return resultInfo;
    }

    public static ResultInfo newFailResultInfo(){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(602);
        resultInfo.setMsg("fail");
        return resultInfo;
    }

    public static ResultInfo newFailResultInfo(String msg){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(602);
        resultInfo.setMsg(msg);
        return resultInfo;
    }

    public static ResultInfo newRepeatResultInfo(String msg){
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(606);
        resultInfo.setMsg(msg);
        return resultInfo;
    }

}
