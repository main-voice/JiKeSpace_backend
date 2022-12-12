package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: JiKeSpace
 * @description: images for transaction
 * @packagename: com.tjsse.jikespace.entity
 * @author: peng peng
 * @date: 2022-12-10 16:23
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "jk_transaction_images")
public class TransactionImages {
    private Long id;
    private Long transactionId;
    private String imageUrl;
}
