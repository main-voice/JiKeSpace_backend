package com.tjsse.jikespace.service;

/**
 * @program: JiKeSpace
 * @description: 登录相关逻辑
 * @packagename: com.tjsse.jikespace.service
 * @author: peng peng
 * @date: 2022-12-02 10:49
 **/
public interface LoginService {
    public String createTokenByUsername(String username, String password);
    public String createTokenByEmail(String email, String password);

}
