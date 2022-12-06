package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/6 15:58
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyOnCommentDTO {
    private Long commentId;
    private String content;
}
