package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 显示帖子信息
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/4 22:21
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDataDTO {
    private Long Id;
    private Integer offset;
    private Integer limit;
}
