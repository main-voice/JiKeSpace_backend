package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/6 20:53
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectSectionVO {
    private Long sectionId;
    private String name;
    private String avatar;
    private String summary;
}
