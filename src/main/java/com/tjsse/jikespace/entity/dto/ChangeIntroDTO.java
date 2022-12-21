package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/21 18:25
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeIntroDTO {
    private Long sectionId;
    private String sectionIntro;
}
