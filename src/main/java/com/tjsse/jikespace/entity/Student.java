package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/18 14:47
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "jk_student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer studentId;
    private String studentPic;
    private String username;
    private Long userId;
    private Boolean isPassed;
}
