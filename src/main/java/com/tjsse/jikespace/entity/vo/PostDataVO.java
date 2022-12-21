package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * SectionDataVO中所需要的post类
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 13:52
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDataVO {
    private Long id;

    private Integer commentCounts;

    private String poster; //发帖人用户名

    private LocalDateTime updateTime;  //该帖子最新回复时间，如果没有回复，则为发帖时间。

    private String title;

    private String summary;
}
