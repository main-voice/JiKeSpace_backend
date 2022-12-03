package com.tjsse.jikespace.config.filter;

import com.tjsse.jikespace.auth_user.AppAdmin;
import com.tjsse.jikespace.auth_user.AppUser;
import com.tjsse.jikespace.entity.Admin;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.mapper.AdminMapper;
import com.tjsse.jikespace.mapper.UserMapper;
import com.tjsse.jikespace.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @program: JiKeSpace
 * @description: 验证token有效性
 * @packagename: com.tjsse.jikespace.config.filter
 * @author: peng peng
 * @date: 2022-12-01 23:08
 **/

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final UserMapper userMapper;

    private final AdminMapper adminMapper;

    public JwtAuthenticationTokenFilter(UserMapper userMapper, AdminMapper adminMapper) {
        this.userMapper = userMapper;
        this.adminMapper = adminMapper;
    }

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
        Map<String, Object> map = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            map = new HashMap<>(claims);
            userid = map.get("subject").toString();
            if (userid == null) {
                System.out.println("userid is null at jwtAuthenticationTokenFilter.java");
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // TODO 下面根据登录信息生成用户，需要根据token，得到用户的角色，动态生成authenticationToken
        String role = map.get("role").toString();
        if (Objects.equals(role, "admin")) {
            Admin admin = adminMapper.selectById(Integer.parseInt(userid));
            if (admin == null) {
                throw new RuntimeException("we can't find the admin");
            }
            AppAdmin loginAdmin = new AppAdmin(admin);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginAdmin, null, null);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        else if (Objects.equals(role, "user")) {
            User user = userMapper.selectById(Integer.parseInt(userid));

            if (user == null) {
                throw new RuntimeException("用户名未登录");
            }

            AppUser loginUser = new AppUser(user);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, null);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
