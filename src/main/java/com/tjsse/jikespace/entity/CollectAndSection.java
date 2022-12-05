package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户收藏的版块的信息
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 15:17
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "jk_collect_section")
public class CollectAndSection {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long sectionId;
}
