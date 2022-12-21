package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/13 23:09
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPostContentVO {
    private Long postId;
    private String updateTime;
    private Integer commentCount;
    private String sectionName;
    private String title;
}
