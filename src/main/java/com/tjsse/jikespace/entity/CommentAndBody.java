package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论和内容的联系类
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/5 16:33
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "jk_comment_body")
public class CommentAndBody {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String content;
    private String contentHtml;
    private Long commentId;
}
