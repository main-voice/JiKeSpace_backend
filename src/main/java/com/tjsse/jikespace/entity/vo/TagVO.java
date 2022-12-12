package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: JiKeSpace
 * @description: vo for tags
 * @packagename: com.tjsse.jikespace.entity.vo
 * @author: peng peng
 * @date: 2022-12-10 14:36
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagVO {
    Long tagId;
    String tagName;
    List<SubtagVO> subtagList;
}
