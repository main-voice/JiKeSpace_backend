package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/2 23:41
 * @since JDK18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "jk_section")
public class Section {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String sectionName;

    private Long adminId;

    private String sectionSummary;

    private String sectionAvater;

    private Integer postCounts;

    private Integer userCounts;
}
