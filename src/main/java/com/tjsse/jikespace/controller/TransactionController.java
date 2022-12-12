package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.entity.dto.NewTransactionDTO;
import com.tjsse.jikespace.entity.dto.SearchTransactionDTO;
import com.tjsse.jikespace.service.TransactionService;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @program: JiKeSpace
 * @description: controller for transaction
 * @packagename: com.tjsse.jikespace.controller
 * @author: peng peng
 * @date: 2022-12-10 14:29
 **/

@RestController
@RequestMapping("transaction/")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("tags/all/")
    public Result getAllTabs() {
        return transactionService.getAllTags();
    }

    @GetMapping("posts/")
    public Result getTransactionPost(@RequestBody SearchTransactionDTO searchTransactionDTO) {
        return transactionService.getTransactionInfoByPage(searchTransactionDTO);
    }

    @GetMapping("post/{id}/")
    public Result getTransactionPostById(@PathVariable(value = "id") Long id) {
        return transactionService.getTransactionInfoById(id);
    }

    @PostMapping("publish/")
    public Result createNewTransactionPost(@RequestParam("type") String type,
                                           @RequestParam("title") String title,
                                           @RequestParam("price") Integer price,
                                           @RequestParam("tag_id") Integer tagId,
                                           @RequestParam("subtag_id") Integer subtagId,
                                           @RequestParam("content") String content,
                                           @RequestParam("campus_zone") String campus,
                                           @RequestParam("contact_type") String contactType,
                                           @RequestParam("contact_number") String contactNumber,
                                           @RequestParam("image")MultipartFile[] multipartFiles,
                                           @RequestHeader("JK-Token") String token)
    {
        NewTransactionDTO newTransactionDTO = NewTransactionDTO.builder()
                .type(type)
                .title(title)
                .price(price)
                .tagId(tagId)
                .subtagId(subtagId)
                .content(content)
                .campus(campus)
                .contactType(contactType)
                .contactNumber(contactNumber)
                .multipartFiles(multipartFiles).build();
        Long userId = Long.valueOf(Objects.requireNonNull(JwtUtil.getUserIdFromToken(token)));
        return transactionService.createTransactionPost(userId, newTransactionDTO);
    }

    @PostMapping("delete/")
    public Result deleteTransactionPost(@RequestParam("id") Long id) {
        return transactionService.deleteTransactionPost(id);
    }

}