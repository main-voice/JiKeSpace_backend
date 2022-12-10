package com.tjsse.jikespace.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: JiKeSpace
 * @description: vo for a specific transaction post
 * @packagename: com.tjsse.jikespace.entity.vo
 * @author: peng peng
 * @date: 2022-12-10 22:35
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionVO {
    private Long id;
// author info
    private String authorName;
    private String avatar;

    private String title;
    private Integer price;
    private String content;
    private String campus;

    private List<String> imagesList;

    private String contactType;
    private String contactNumber;

    private LocalDateTime publishTime;
    private Integer viewCounts;
}
