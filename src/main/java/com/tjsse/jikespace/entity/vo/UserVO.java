package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: JiKeSpace
 * @description: 返回给前端的用户类
 * @packagename: com.tjsse.jikespace.entity.vo
 * @author: peng peng
 * @date: 2022-12-02 15:26
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private String name;
    private String avatar;
    private List<String> roles;
    private Boolean isAuthenticated;
}
