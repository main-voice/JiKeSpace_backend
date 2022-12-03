package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签数据
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 13:33
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "jk_tag")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String tagName;
}
