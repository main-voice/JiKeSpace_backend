package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: JiKeSpace
 * @description: subtag for post
 * @packagename: com.tjsse.jikespace.entity
 * @author: peng peng
 * @date: 2022-12-05 20:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "jk_subtag")
public class Subtag {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String subtagName;

    private Long tagId;

}
