package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: JiKeSpace
 * @description: 传输过来的用户类
 * @packagename: com.tjsse.jikespace.entity.dto
 * @author: peng peng
 * @date: 2022-12-02 00:28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    String username;
    String password;
    String email;
}
