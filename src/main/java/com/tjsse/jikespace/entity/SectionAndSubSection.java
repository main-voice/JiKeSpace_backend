package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * section与subsection的联系集
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/4 11:07
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "jk_section_subsection")
public class SectionAndSubSection {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long section_id;

    private Long subsection_id;
}
