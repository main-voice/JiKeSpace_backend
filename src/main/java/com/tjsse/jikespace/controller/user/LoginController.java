package com.tjsse.jikespace.controller.user;

import com.tjsse.jikespace.entity.dto.UserDTO;
import com.tjsse.jikespace.service.LoginService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        String email = userDTO.getEmail();
        return Result.success(loginService.createTokenByEmail(email, password));
//        return Result.success(loginService.createTokenByUsername(username, password));
    }
}
