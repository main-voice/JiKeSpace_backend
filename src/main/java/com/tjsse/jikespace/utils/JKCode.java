package com.tjsse.jikespace.utils;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/11/27 20:59
 * @since JDK18
 */

public enum JKCode {
    SUCCESS(20000, "请求成功"),
    PARAMS_ERROR(10001,"参数有误"),
    ACCOUNT_NOT_EXIST(10002,"用户名不存在"),
    PWD_ERROR(10003, "密码错误"),
    TOKEN_ERROR(10004,"token不合法"),
    ACCOUNT_EXIST(10005,"账号已存在"),
    NO_PERMISSION(70001,"无访问权限"),
    SESSION_TIME_OUT(90001,"会话超时"),
    NO_LOGIN(90002,"未登录"),
    OTHER_ERROR(-1, "总之就是出错"),

    // user status
    LOG_IN(20, "已登录"),
    LOG_OUT(30, "已登出"),
    BANNED(40, "已封禁"),

    SELL_POST(50, "出售帖"),
    BUY_POST(60, "求购帖"),

    // 存储到redis缓存时，对key加入统一前缀
    EMAIL_CODE_PREFIX(70, "email-"),
    TEL_CODE_PREFIX(80, "tel-");

    private Integer code;
    private String msg;

    JKCode(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}



