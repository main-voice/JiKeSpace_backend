package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: JiKeSpace
 * @description: entity for table 'jk_collect_transaction_post'
 * @packagename: com.tjsse.jikespace.entity
 * @author: peng peng
 * @date: 2022-12-14 20:51
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "jk_collect_transaction_post")
public class CollectAndTransaction {
    Long id;
    Long userId;
    Long transactionPostId;
}
