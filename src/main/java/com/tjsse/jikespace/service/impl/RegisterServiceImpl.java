package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.mapper.UserMapper;
import com.tjsse.jikespace.service.FolderService;
import com.tjsse.jikespace.service.RegisterService;
import com.tjsse.jikespace.utils.JKCode;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    FolderService folderService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Result register(String username, String password, String email) {
        Map<String, String> map = new HashMap<>();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .or()
                .eq("email", email);
        List<User> userList = new ArrayList<>();
        userList = userMapper.selectList(queryWrapper);
        if (userList.size() >= 1) {
            return Result.fail(JKCode.ACCOUNT_EXIST.getCode(), "用户已存在", null);
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .isDeleted(false)
                .isModerator(false)
                .build();

        int insert = userMapper.insert(user);
        if (insert == 0 || insert < 0) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "用户注册失败");
        }
        else {
            folderService.createFolder(user.getId(),"默认收藏夹");
            user.setNickname("用户"+user.getId().toString());
            userMapper.updateById(user);
        }

        return Result.success(20000,"okk",null);
    }
}
