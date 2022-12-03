package com.tjsse.jikespace.auth_user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tjsse.jikespace.entity.Admin;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @program: JiKeSpace_backend
 * @description: service for app admin auth
 * @package_name: com.tjsse.jikespace.auth_user
 * @author: peng peng
 * @date: 2022/12/3
 **/
@Service
public class AppAdminService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String adminName) throws UsernameNotFoundException {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        System.out.println(adminName);
        queryWrapper.eq("admin_name", adminName);
        Admin admin = adminMapper.selectOne(queryWrapper);
        if (admin == null) {
            throw new UsernameNotFoundException("user不存在");
        }
        return new AppAdmin(admin);
    }
}
