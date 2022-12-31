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
 * @description:
 * @packagename: com.tjsse.jikespace.entity
 * @author: peng peng
 * @date: 2022-12-21 19:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "jk_transaction_body")
public class TransactionAndBody {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String content;
    private Long transactionId;
}
