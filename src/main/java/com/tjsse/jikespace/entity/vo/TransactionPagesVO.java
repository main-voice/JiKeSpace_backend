package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: JiKeSpace
 * @description: vo for returning a list of transactionPage entity
 * @packagename: com.tjsse.jikespace.entity.vo
 * @author: peng peng
 * @date: 2022-12-14 21:39
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPagesVO {
    Integer total;
    List<TransactionPageVO> transactionPageVOList;
}
