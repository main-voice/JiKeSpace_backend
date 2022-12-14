package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/14 9:20
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionPostsVO {
    private Integer total;
    private List<PostDataVO>  postDataVOList;
}
