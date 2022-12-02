package com.tjsse.jikespace.service.impl;

import com.tjsse.jikespace.auth_user.AppUser;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.entity.vo.UserVO;
import com.tjsse.jikespace.service.UserInfoService;
import com.tjsse.jikespace.utils.Result;
import com.tjsse.jikespace.utils.StatusCode;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.tjsse.jikespace.utils.StatusCode.*;

/**
 * @program: JiKeSpace
 * @description:
 * @packagename: com.tjsse.jikespace.service.impl
 * @author: peng peng
 * @date: 2022-12-02 15:24
 **/

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public Result getUserInfo() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        // get context
// login email token email database id
        AppUser loginUser = (AppUser) usernamePasswordAuthenticationToken.getPrincipal();
        User user = loginUser.getUser();

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), userVO);
    }
}
