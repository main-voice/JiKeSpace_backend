package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * post和tag的联系类
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 20:46
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "jk_post_tag")
public class PostTag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long tagId;
    private Long PostId;
}
