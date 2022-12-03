package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  通过标签来筛选帖子
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 20:11
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostsWithTagDTO {
    private Long sectionId;
    private Long tagId;
    private Integer curPage;
    private Integer limit;
}
