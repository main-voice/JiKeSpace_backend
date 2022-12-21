package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 传输给后端的创建版块所需的数据
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 11:37
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionCreateDTO {
    private Long userId;

    private String sectionName;

    private String sectionSummary;

    private String sectionAvater;
}
