package com.tjsse.jikespace.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.entity.dto.*;
import com.tjsse.jikespace.mapper.UserMapper;
import com.tjsse.jikespace.service.*;
import com.tjsse.jikespace.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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
    private PostService postService;
    @Autowired
    private OssService ossService;

    @Autowired
    EmailService emailService;
    @Autowired
    private  FolderService folderService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("info")
    public Result getUserInfo(@RequestHeader("JK-Token") String token) {
        return userInfoService.getUserInfo(token);
    }

    @GetMapping("send-email-code")
    public Result sendEmailCode(@RequestParam(value = "email") String email) {
        RedisUtils redisUtils = new RedisUtils(stringRedisTemplate);
        if (email == null) {
            return Result.fail(OTHER_ERROR.getCode(), "邮箱为空", null);
        }
        return userInfoService.sendEmailVerifyCode(email);
    }

    @PostMapping("forget-pwd")
    public Result forgetPassword(@RequestBody JSONObject jsonObject) throws JSONException {
        String email = jsonObject.getString("email");
        String verifyCode = jsonObject.getString("verifyCode");
        String newPassword = jsonObject.getString("newPassword");

        if (email == null || verifyCode == null || newPassword == null) {
            return Result.fail(PARAMS_ERROR.getCode(), PARAMS_ERROR.getMsg(), null);
        }
        return userInfoService.forgetPassword(verifyCode, email, newPassword);
    }

    @PostMapping("account/edit_email")
    public Result editEmail(@RequestHeader("JK-Token") String jk_token, @RequestBody EditEmailDTO editEmailDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return userInfoService.editEmail(userId,editEmailDTO);
    }

    @GetMapping("account/get_user_info")
    public Result getUserInformation(@RequestHeader("JK-Token") String jk_token){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return userInfoService.getUserInformation(userId);
    }

    @PostMapping("account/edit_info")
    public Result editUserInfo(@RequestHeader("JK-Token") String jk_token, @RequestBody UserInfoDTO userInfoDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return userInfoService.editUserInfo(userId,userInfoDTO);
    }

    @PostMapping("account/edit_password")
    public Result editPassword(@RequestHeader("JK-Token") String jk_token, @RequestBody PasswordDTO passwordDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return userInfoService.editPassword(userId,passwordDTO);
    }

    @PostMapping("account/create_folder")
    public Result createFolder(@RequestHeader("JK-Token") String jk_token, @RequestBody Map<String,String> map){
        String folderName = map.get("folderName");
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return folderService.createFolder(userId,folderName);
    }

    @PostMapping("account/rename_folder")
    public Result renameFolder(@RequestHeader("JK-Token") String jk_token, @RequestBody RenameFolderDTO renameFolderDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        return folderService.renameFolder(renameFolderDTO);
    }

    @GetMapping("account/get_folders")
    public Result getFolders(@RequestHeader("JK-Token") String jk_token){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return folderService.getFolders(userId);
    }

    @GetMapping(value = "account/get_collect_info")
    public Result getCollectInfo(@RequestHeader("JK-Token") String jk_token,Long folderId
            ,Integer curPage, Integer limit){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        FolderPostDTO folderPostDTO = new FolderPostDTO(folderId,curPage,limit);
        return folderService.getCollectInfo(userId,folderPostDTO);
    }

    @DeleteMapping("account/delete_folder")
    public Result deleteFolder(@RequestHeader("JK-Token") String jk_token,@RequestBody Map<String,String> map){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        Long folderId = Long.valueOf(map.get("folderId"));
        return folderService.deleteFolder(userId,folderId);
    }

    @PostMapping("account/change_avatar")
    public Result changeAvatar(@RequestHeader("JK-Token") String jk_token,@RequestParam("Avatar") MultipartFile avatar){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.valueOf(userIdStr);

        String s = ossService.uploadFile(avatar);
        User user = userInfoService.findUserById(userId);
        user.setAvatar(s);
        threadService.updateUserByAvatar(userMapper,user);
        Map<String,String> map = new HashMap<>();
        map.put("result",s);

        return Result.success(20000,"okk",map);
    }

    @GetMapping("account/get_my_post")
    public Result getMyPost(@RequestHeader("JK-Token") String jk_token,String type,Integer curPage,Integer limit){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.valueOf(userIdStr);
        return  postService.findPostsByUserIdWithPage(userId,type,curPage,limit);
    }

}

