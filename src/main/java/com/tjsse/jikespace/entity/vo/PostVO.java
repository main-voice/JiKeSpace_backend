package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 一个帖子的详细信息
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/4 23:26
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostVO {
    private String title;
    private Boolean isCollected;
    private Long sectionId;
    private String sectionName;
    private Long subsectionId;
    private String subsectionName;
    private Integer commentCounts;
    private List<CommentVO> commentVOList;
}
