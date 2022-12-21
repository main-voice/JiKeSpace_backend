package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/13 22:26
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataVO {
    private String username;
    private String nickname;
    private String summary;
    private String email;
    private String gender;
    private String phoneNumber;
    private String avatar;
    private String studentId;
}
