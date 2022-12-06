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
 * @description: post for transaction
 * @packagename: com.tjsse.jikespace.entity
 * @author: peng peng
 * @date: 2022-12-05 19:20
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "jk_transaction_post")
public class TransactionPost {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String summary;

    private Long authorId;
    private Long bodyId;
    private Integer tagId;
    private Integer subTagId;

    private String contactInfo;

    private Integer commentCounts;
    private Integer viewCounts;

    private boolean isPinned;
    private boolean isDeleted;

    private Integer postType;

}
