package com.tjsse.jikespace.config.filter;

import com.tjsse.jikespace.auth_user.AppUser;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.mapper.UserMapper;
import com.tjsse.jikespace.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @program: JiKeSpace
 * @description: 验证token有效性
 * @packagename: com.tjsse.jikespace.config.filter
 * @author: peng peng
 * @date: 2022-12-01 23:08
 **/

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
//        String token = request.getHeader("Authorization");
        String token = request.getHeader("JK-Token");

        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // 直接拒绝请求
            return;
        }

        token = token.substring(7);

        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        User user = userMapper.selectById(Integer.parseInt(userid));

        if (user == null) {
            throw new RuntimeException("用户名未登录");
        }

        AppUser loginUser = new AppUser(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
