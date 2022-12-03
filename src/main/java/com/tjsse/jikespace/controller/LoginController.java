package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.entity.dto.UserDTO;
import com.tjsse.jikespace.service.LoginService;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.Result;
import com.tjsse.jikespace.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @program: JiKeSpace
 * @description:
 * @packagename: com.tjsse.jikespace.controller.user
 * @author: peng peng
 * @date: 2022-12-02 10:53
 **/

@RestController
@RequestMapping("user/")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("login/")
    public Result login(@RequestBody UserDTO userDTO) {
        String password = userDTO.getPassword();
        String email = userDTO.getEmail();
        return Result.success(loginService.createTokenByEmail(email, password));
    }

    @PostMapping("logout/")
    public Result logout(@RequestHeader("JK-Token") String jk_token) {
        Integer userId = JwtUtil.getUserIdFromToken(jk_token);
        if (userId == null) {
            return Result.fail(StatusCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        return loginService.logout(userId);
    }
}
