package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/20 16:49
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenameSubSectionDTO {
    private Long userId;
    private Long subsectionId;
    private String name;
}
