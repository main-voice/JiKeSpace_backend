package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件夹下帖子列表
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/9 13:40
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderPostVO {
    private Long postId;
    private LocalDateTime time;
    private String posterName;
    private String sectionName;
    private String title;
}
