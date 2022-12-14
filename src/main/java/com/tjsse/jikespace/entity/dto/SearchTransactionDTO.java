package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: JiKeSpace
 * @description: dto for transaction
 * @packagename: com.tjsse.jikespace.entity.dto
 * @author: peng peng
 * @date: 2022-12-10 15:42
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchTransactionDTO {
    String searchContent;
    String campusZone;
    Long tagId;
    Long subtagId;
    int offset; // 分页查询时的起始位置
    int limit;
    String type; // 交易还是求购
}
