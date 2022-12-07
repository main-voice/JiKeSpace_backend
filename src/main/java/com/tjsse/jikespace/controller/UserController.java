package com.tjsse.jikespace.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tjsse.jikespace.service.EmailService;
import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.RedisUtils;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import static com.tjsse.jikespace.utils.JKCode.OTHER_ERROR;
import static com.tjsse.jikespace.utils.JKCode.PARAMS_ERROR;

/**
 * @program: JiKeSpace
 * @description: 获取用户基本信息
 * @packagename: com.tjsse.jikespace.controller.user
 * @author: peng peng
 * @date: 2022-12-02 15:21
 **/
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserService userInfoService;

    @Autowired
    EmailService emailService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("info/")
    public Result getUserInfo() {
        Result result = new Result();
        result = userInfoService.getUserInfo();
        return result;
    }


    @GetMapping("send-email-code/")
    public Result sendEmailCode(@RequestParam(value = "email") String email) {
        RedisUtils redisUtils = new RedisUtils(stringRedisTemplate);
        if (email == null) {
            return Result.fail(OTHER_ERROR.getCode(), "邮箱为空", null);
        }
        return userInfoService.sendEmailVerifyCode(email);
    }

    @PostMapping("reset-pwd/")
    public Result forgetPassword(@RequestBody JSONObject jsonObject) throws JSONException {
        String email = jsonObject.getString("email");
        String verifyCode = jsonObject.getString("verifyCode");
        String newPassword = jsonObject.getString("newPassword");

        if (email == null || verifyCode == null || newPassword == null) {
            return Result.fail(PARAMS_ERROR.getCode(), PARAMS_ERROR.getMsg(), null);
        }
        return userInfoService.resetPassword(verifyCode, email, newPassword);
    }
}
