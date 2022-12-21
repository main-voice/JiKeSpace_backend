package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏帖子功能--前端传来的数据
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/9 22:59
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectPostDTO {
    private Long id;
    private Long folderId;
}
