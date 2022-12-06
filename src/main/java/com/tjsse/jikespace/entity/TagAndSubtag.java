package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: JiKeSpace
 * @description: connection for tag and subtag
 * @packagename: com.tjsse.jikespace.entity
 * @author: peng peng
 * @date: 2022-12-05 20:22
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "jk_tag_subtag")
public class TagAndSubtag {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tagId;
    private Long subtagId;
}
