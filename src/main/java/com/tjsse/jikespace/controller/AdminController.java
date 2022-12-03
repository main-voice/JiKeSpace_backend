package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.entity.Admin;
import com.tjsse.jikespace.service.LoginService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: JiKeSpace_backend
 * @description: controller for admin
 * @package_name: com.tjsse.jikespace.controller
 * @author: peng peng
 * @date: 2022/12/3
 **/

@RestController
@RequestMapping("admin/")
public class AdminController {
    @Autowired
    private LoginService loginService;

    @PostMapping("login/")
    public Result login(@RequestBody Admin admin) {
        String password = admin.getPassword();
        String adminName = admin.getAdminName();
//        System.out.println(password + " , " + adminName);
        return loginService.createTokenByAdminName(adminName, password);
    }

    @GetMapping("info/")
    public String eq() {
        return "hello world";
    }

}