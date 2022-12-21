package com.tjsse.jikespace.entity.vo;

import com.tjsse.jikespace.entity.dto.FolderPostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 前端显示用户收藏的帖子功能所需要的数据
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/14 10:45
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectPostsVO {
    private Integer total;
    private List<FolderPostVO> folderPostDTOList;
}
