package com.tjsse.jikespace.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: JiKeSpace
 * @description: 对返回值封装
 * @packagename: com.tjsse.jikespace.entity.vo
 * @author: peng peng
 * @date: 2022-11-29 18:15
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private boolean success;

    private Integer code;

    private String msg;

    private Object data;


    public static Result success() {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(StatusCode.SUCCESS.getCode());
        result.setMsg("");
        result.setData(null);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(StatusCode.SUCCESS.getCode());
        result.setMsg("");
        result.setData(data);
        return result;
    }

    public static Result success(Integer code, Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setMsg("");
        result.setData(data);
        return result;
    }
    public static Result success(Integer code, String msg, Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }


    public static Result fail() {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(null);
        result.setMsg("");
        result.setData(null);
        return result;
    }

    public static Result fail(Object data) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(null);
        result.setMsg("");
        result.setData(data);
        return result;
    }

    public static Result fail(Integer code, Object data) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg("");
        result.setData(data);
        return result;
    }
    public static Result fail(Integer code, String msg, Object data) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
