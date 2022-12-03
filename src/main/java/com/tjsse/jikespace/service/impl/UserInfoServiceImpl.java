package com.tjsse.jikespace.service.impl;

import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.entity.vo.UserVO;
import com.tjsse.jikespace.mapper.UserMapper;
import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.tjsse.jikespace.utils.StatusCode.*;

/**
 * @program: JiKeSpace
 * @description:
 * @packagename: com.tjsse.jikespace.service.impl
 * @author: peng peng
 * @date: 2022-12-02 15:24
 **/

@Service
public class UserInfoServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result getUserInfo(Integer userId) {

        User user = userMapper.selectById(userId);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), userVO);
    }
}
