package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/4 23:31
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "jk_comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long bodyId;
    private Long postId;
    private Long authorId;
    private String type;
    private Boolean isDeleted;
    private LocalDateTime updateTime;
}
