package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/6 16:07
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("jk_post_comment")
public class PostAndComment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long commentId;
    private Long postId;
}
