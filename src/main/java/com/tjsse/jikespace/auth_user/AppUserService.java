package com.tjsse.jikespace.auth_user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @program: JiKeSpace
 * @description: 主要用于接入数据库得到用户信息
 * @packagename: com.tjsse.jikespace.auth_user
 * @author: peng peng
 * @date: 2022-12-01 23:19
 **/

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        System.out.println(username);
        queryWrapper.eq("email", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("user不存在");
        }
        return new AppUser(user);
    }
}
