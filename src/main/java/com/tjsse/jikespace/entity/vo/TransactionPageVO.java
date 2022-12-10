package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @program: JiKeSpace
 * @description: vo for transaction(when query with page)
 * @packagename: com.tjsse.jikespace.entity.vo
 * @author: peng peng
 * @date: 2022-12-10 16:09
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPageVO {
    private Long id;
    private String title;
    private Integer price;
    private String coverImage;
    private String campus;
    private String summary;
}
