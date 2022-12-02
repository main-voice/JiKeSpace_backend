package com.tjsse.jikespace.service.impl;

import com.tjsse.jikespace.auth_user.AppUser;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.service.LoginService;
import com.tjsse.jikespace.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
    private AuthenticationManager authenticationManager;

    @Override
    public String createTokenByUsername(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        AppUser appUser = (AppUser) authenticate.getPrincipal();
        User user = appUser.getUser();

        String jwt = JwtUtil.createJWT(user.getId().toString());

        return jwt;
    }

    @Override
    public String createTokenByEmail(String email, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        AppUser appUser = (AppUser) authenticate.getPrincipal();
        User user = appUser.getUser();

        String jwt = JwtUtil.createJWT(user.getId().toString());

        return jwt;
    }
}
