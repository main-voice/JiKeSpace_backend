package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author peng peng
 * @date 2022/12/3 19:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "jk_admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String adminName;

    private String password;

}