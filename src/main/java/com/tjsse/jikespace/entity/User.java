package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: JiKeSpace
 * @description: 用户实体类
 * @packagename: com.tjsse.jikespace.entity
 * @author: peng peng
 * @date: 2022-11-29 17:52
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "jk_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String isModerator;

    private String studentId;

    private String summary;

    private String avatar;

    private LocalDateTime createTime;

    private Integer deleted;

    private String email;

    private LocalDateTime lastLoginTime;

    private String phoneNumber;

    private String nickname;

    private String password;

    private String gender;

    private String status;

}
