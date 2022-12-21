package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 寻找文件夹中的帖子
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/9 0:29
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderPostDTO {
    private Long folderId;
    private Integer curPage;
    private Integer limit;
}
