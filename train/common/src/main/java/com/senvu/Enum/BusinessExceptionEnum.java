package com.senvu.Enum;


public enum BusinessExceptionEnum {
    MEMBER_MOBILE_EXIST("手机号已注册"),
    MEMBER_MOBILE_NOT_EXIST("请先获取验证码"),
    MEMBER_CODE_ERROR("短信验证码错误"),
    STATION_EXITED_ERROR("车站已存在"),
    CARRIAGE_EXITED_ERROR("同车次车厢已存在"),
    DAILY_CARRIAGE_EXITED_ERROR("同日同车次车厢已存在"),
    TRAIN_EXITED_ERROR("车次已存在"),
    DAILY_TRAIN_EXITED_ERROR("同日车次已存在"),
    TRAIN_STATION_EXITED_ERROR("同车次站名已存在"),
    DAILY_TRAIN_STATION_EXITED_ERROR("同日同车次站名已存在");

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BussinessExceptionEnum{" +
                "desc='" + desc + '\'' +
                '}';
    }

    private String desc;

    public String getDesc() {
        return desc;
    }
}
