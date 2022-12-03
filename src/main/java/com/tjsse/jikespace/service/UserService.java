package com.tjsse.jikespace.service;

import com.tjsse.jikespace.utils.Result;

/**
 * @program: JiKeSpace
 * @description:
 * @packagename: com.tjsse.jikespace.service
 * @author: peng peng
 * @date: 2022-12-02 15:22
 **/
public interface UserService {
    Result getUserInfo(Integer userId);
}
