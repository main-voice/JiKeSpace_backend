package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发表帖子时后端需要的数据
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/4 15:27
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPublishDTO {
    private Long sectionId;
    private String title;
    private Long subsectionId;
    private String content;
}
