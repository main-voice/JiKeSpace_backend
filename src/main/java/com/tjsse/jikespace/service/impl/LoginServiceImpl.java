package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tjsse.jikespace.entity.Admin;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.mapper.AdminMapper;
import com.tjsse.jikespace.mapper.UserMapper;
import com.tjsse.jikespace.service.LoginService;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.tjsse.jikespace.utils.StatusCode.*;

/**
 * @program: JiKeSpace
 * @description:
 * @packagename: com.tjsse.jikespace.service.impl
 * @author: peng peng
 * @date: 2022-12-02 10:50
 **/

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Result createTokenByAdminName(String adminName, String password) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("admin_name", adminName);
        Admin admin = adminMapper.selectOne(queryWrapper);
        if (admin == null) {
            return Result.fail(ACCOUNT_NOT_EXIST.getCode(), ACCOUNT_EXIST.getMsg(), null);
        }

        String jwt = JwtUtil.createJWT(admin.getId().toString(), "admin");

        if(jwt == null) {
            return Result.fail(OTHER_ERROR.getCode(), "token 生成失败", null);
        }
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), jwt);
    }

    @Override
    public Result createTokenByEmail(String email, String password) {
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(email, password);
//
//        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//
//        AppUser appUser = (AppUser) authenticate.getPrincipal();
//        User user = appUser.getUser();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return Result.fail(ACCOUNT_NOT_EXIST.getCode(), ACCOUNT_EXIST.getMsg(), null);
        }
        // 修改用户登录信息
        LocalDateTime lastLoginTime = LocalDateTime.now();

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", user.getId())
                .set("last_login_time", lastLoginTime)
                .set("status", LOG_IN.getCode());
        userMapper.update(null, userUpdateWrapper);

        String jwt = JwtUtil.createJWT(user.getId().toString(), "user");

        if(jwt == null) {
            return Result.fail(OTHER_ERROR.getCode(), "token 生成失败", null);
        }
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), jwt);
    }

    @Override
    public Result logout(Integer userId) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", userId)
                .ne("status", LOG_OUT.getCode())
                .set("status", LOG_OUT.getCode());
        int res = userMapper.update(null, userUpdateWrapper);
        if (res == 0) {
            return Result.fail(OTHER_ERROR.getCode(), "用户未找到或该用户已经登出", null);
        } else if (res > 1) {
            return Result.fail(OTHER_ERROR.getCode(), "登出多个用户", null);
        } else {
            return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), null);
        }
    }
}
