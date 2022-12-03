package com.tjsse.jikespace.service;

import com.tjsse.jikespace.utils.Result;

/**
 * @program: JiKeSpace
 * @description: 登录相关逻辑
 * @packagename: com.tjsse.jikespace.service
 * @author: peng peng
 * @date: 2022-12-02 10:49
 **/
public interface LoginService {
    public Result createTokenByAdminName(String username, String password);
    public Result createTokenByEmail(String email, String password);

    public Result logout(Integer userId);

}
