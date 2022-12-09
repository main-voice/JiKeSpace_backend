package com.tjsse.jikespace.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tjsse.jikespace.entity.Folder;
import com.tjsse.jikespace.entity.dto.*;
import com.tjsse.jikespace.service.EmailService;
import com.tjsse.jikespace.service.FolderService;
import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.JKCode;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.RedisUtils;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    private  FolderService folderService;

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

    @PostMapping("forget-pwd/")
    public Result forgetPassword(@RequestBody JSONObject jsonObject) throws JSONException {
        String email = jsonObject.getString("email");
        String verifyCode = jsonObject.getString("verifyCode");
        String newPassword = jsonObject.getString("newPassword");

        if (email == null || verifyCode == null || newPassword == null) {
            return Result.fail(PARAMS_ERROR.getCode(), PARAMS_ERROR.getMsg(), null);
        }
        return userInfoService.forgetPassword(verifyCode, email, newPassword);
    }

    @PostMapping("account/edit_email/")
    public Result editEmail(@RequestHeader("JK-Token") String jk_token, @RequestBody EditEmailDTO editEmailDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return userInfoService.editEmail(userId,editEmailDTO);
    }

    @GetMapping("account/get_user_info/")
    public Result getUserInformation(@RequestHeader("JK-Token") String jk_token){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return userInfoService.getUserInformation(userId);
    }

    @PostMapping("account/edit_info/")
    public Result editUserInfo(@RequestHeader("JK-Token") String jk_token, @RequestBody UserInfoDTO userInfoDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return userInfoService.editUserInfo(userId,userInfoDTO);
    }

    @PostMapping("account/edit_password/")
    public Result editPassword(@RequestHeader("JK-Token") String jk_token, @RequestBody PasswordDTO passwordDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return userInfoService.editPassword(userId,passwordDTO);
    }

    @PostMapping("account/create_folder/")
    public Result createFolder(@RequestHeader("JK-Token") String jk_token, @RequestBody Map<String,String> map){
        String folderName = map.get("folderName");
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return folderService.createFolder(userId,folderName);
    }

    @PostMapping("account/rename_folder/")
    public Result renameFolder(@RequestHeader("JK-Token") String jk_token, @RequestBody RenameFolderDTO renameFolderDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        return folderService.renameFolder(renameFolderDTO);
    }

    @GetMapping("account/get_folders/")
    public Result getFolders(@RequestHeader("JK-Token") String jk_token){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return folderService.getFolders(userId);
    }

    @GetMapping("account/get_collect_info/")
    public Result getCollectInfo(@RequestHeader("JK-Token") String jk_token,@RequestBody FolderPostDTO folderPostDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return folderService.getCollectInfo(userId,folderPostDTO);
    }

    @DeleteMapping("account/delete_folder/")
    public Result deleteFolder(@RequestHeader("JK-Token") String jk_token,@RequestBody Map<String,String> map){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        Long folderId = Long.valueOf(map.get("folderId"));
        return folderService.deleteFolder(userId,folderId);
    }

}

