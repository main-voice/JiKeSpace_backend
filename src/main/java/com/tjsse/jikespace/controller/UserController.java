package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.OssService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    // just for test
    @PostMapping("uploadImg/")
    public List<String> imageUpload(MultipartFile[] imag) {
        List<String> array = new ArrayList<String>();
        for (MultipartFile image1:
             imag) {
            String img = ossService.uploadFile(image1, "dest");
            array.add(img);
        }
        return array;
    }

}
