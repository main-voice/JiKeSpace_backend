package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long id;
    private String username;
    private String summary;
    private String avatar;
    private String email;
    private String phoneNumber;
    private String nickname;
    private String gender;
}
