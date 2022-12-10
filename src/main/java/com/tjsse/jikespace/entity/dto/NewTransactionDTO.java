package com.tjsse.jikespace.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: JiKeSpace
 * @description: dto for creating a transaction
 * @packagename: com.tjsse.jikespace.entity.dto
 * @author: peng peng
 * @date: 2022-12-10 19:50
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewTransactionDTO {
    String type;
    String title;
    Integer price;
    Integer tagId;
    Integer subtagId;
    String content;
    String campus;
    String contactType;
    String contactNumber;
    MultipartFile[] multipartFiles;
}
