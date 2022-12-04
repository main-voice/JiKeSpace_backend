package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.OssService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    OssService ossService;

    @GetMapping("info/")
    public Result getUserInfo() {
        Result result = new Result();
        result = userInfoService.getUserInfo();
        return result;
    }

    @PostMapping("uploadImg/")
    public String imageUpload(@RequestParam("image") MultipartFile image) {
        String img = ossService.uploadFile(image);
        return img;
    }

}
