package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/8 22:57
 * @since JDK18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "jk_folder")
public class Folder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String folderName;
}
