package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private UserService userService;

    @GetMapping("info/")
    public Result getUserInfo(@RequestHeader("JK-Token") String jk_token) {
        Integer userId = JwtUtil.getUserIdFromToken(jk_token);
        Result result = new Result();
        result = userService.getUserInfo(userId);
        return result;
    }

}
