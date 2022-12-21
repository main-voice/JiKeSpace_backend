package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/8 19:55
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private String nickname;
    private String phone;
    private String gender;
    private String intro;
}
