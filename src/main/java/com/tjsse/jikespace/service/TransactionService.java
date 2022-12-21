package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.dto.NewTransactionDTO;
import com.tjsse.jikespace.entity.dto.SearchTransactionDTO;
import com.tjsse.jikespace.utils.Result;

/**
 * @program: JiKeSpace
 * @description: service for transaction
 * @packagename: com.tjsse.jikespace.service
 * @author: peng peng
 * @date: 2022-12-10 14:32
 **/
public interface TransactionService {
    Result getAllTags();

    Result getTransactionInfoByPage(SearchTransactionDTO searchTransactionDTO);

    Result getTransactionInfoById(Long id);
    Result createTransactionPost(Long authorId, NewTransactionDTO newTransactionDTO);

    Result deleteTransactionPost(Long id);

    Result collectTransactionPost(Long userId, Long postId);

    Result getUserSellTrans(Long userId, Integer offset, Integer limit);
    Result getUserSeekTrans(Long userId, Integer offset, Integer limit);

}
