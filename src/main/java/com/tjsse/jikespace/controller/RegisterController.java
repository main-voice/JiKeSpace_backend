package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.entity.dto.UserDTO;
import com.tjsse.jikespace.service.RegisterService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: JiKeSpace
 * @description: 登录接口
 * @packagename: com.tjsse.jikespace.controller.user
 * @author: peng peng
 * @date: 2022-12-01 23:24
 **/

@RestController
@RequestMapping("user/")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("register")
    public Result registerUser(@RequestBody UserDTO userDTO) {

        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        String email = userDTO.getEmail();

        return registerService.register(username, password, email);
    }
}
