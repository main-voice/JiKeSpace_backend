package com.tjsse.jikespace.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.entity.dto.NewTransactionDTO;
import com.tjsse.jikespace.entity.dto.SearchTransactionDTO;
import com.tjsse.jikespace.mapper.UserMapper;
import com.tjsse.jikespace.service.TransactionService;
import com.tjsse.jikespace.utils.JKCode;
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

    @Autowired
    private UserMapper userMapper;

    @GetMapping("tag")
    public Result getAllTags(@RequestHeader(value = "JK-Token") String token) {
        if (!checkStudent(token)) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "用户尚未学生认证", null);
        }
        return transactionService.getAllTags();
    }

    @GetMapping("sale_info")
    public Result getTransactionPost(@RequestHeader(value = "JK-Token") String token,
                                     String searchContent, String campusZone, Long tagId, Long subtagId,
                                     Integer offset, Integer limit, String type) {
        if (!checkStudent(token)) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "用户尚未学生认证", null);
        }

        SearchTransactionDTO searchTransactionDTO = new SearchTransactionDTO();
        if (searchContent != null) {
            searchTransactionDTO.setSearchContent(searchContent);
        }
        if (campusZone != null) {
            searchTransactionDTO.setCampusZone(campusZone);
        }
        if (tagId != null) {
            searchTransactionDTO.setTagId(tagId);
        }
        if (subtagId != null) {
            searchTransactionDTO.setSubtagId(subtagId);
        }
        if (offset != null) {
            searchTransactionDTO.setOffset(offset);
        }
        if (limit != null) {
            searchTransactionDTO.setLimit(limit);
        }
        if (type != null) {
            searchTransactionDTO.setType(type);
        }

        return transactionService.getTransactionInfoByPage(searchTransactionDTO);
    }

    @GetMapping("post")
    public Result getTransactionPostById(@RequestHeader(value = "JK-Token") String token,
                                         Long id) {
        if (!checkStudent(token)) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "用户尚未学生认证", null);
        }
        return transactionService.getTransactionInfoById(id);
    }

    @PostMapping("publish")
    public Result createNewTransactionPost(@RequestParam("type") String type,
                                           @RequestParam("title") String title,
                                           @RequestParam("price") Integer price,
                                           @RequestParam("tagId") Integer tagId,
                                           @RequestParam("subtagId") Integer subtagId,
                                           @RequestParam("content") String content,
                                           @RequestParam("campusZone") String campus,
                                           @RequestParam("contactType") String contactType,
                                           @RequestParam("contactNumber") String contactNumber,
                                           @RequestParam("image")MultipartFile[] multipartFiles,
                                           @RequestHeader("JK-Token") String token)
    {
        if (!checkStudent(token)) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "用户尚未学生认证", null);
        }
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

    @PostMapping("delete")
    public Result deleteTransactionPost(@RequestHeader(value = "JK-Token") String token,
                                        @RequestParam("id") Long id) {
        if (!checkStudent(token)) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "用户尚未学生认证", null);
        }
        return transactionService.deleteTransactionPost(id);
    }

    @PostMapping("collect_transaction")
    public Result collectTransaction(@RequestParam(value = "id") Long postId,
                                     @RequestHeader(value = "JK-Token") String token) {
        if (!checkStudent(token)) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "用户尚未学生认证", null);
        }
        if (postId == null) {
            Result.fail(JKCode.OTHER_ERROR.getCode(), "postId is null", null);
        }
        String userIdFromToken = JwtUtil.getUserIdFromToken(token);
        if (userIdFromToken == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析用户出错", null);
        }
        Long userId = Long.valueOf(userIdFromToken);
        return transactionService.collectTransactionPost(userId, postId);
    }

    boolean checkStudent(String token) {
        String userIdFromToken = JwtUtil.getUserIdFromToken(token);
        assert userIdFromToken != null;
        Long userId = Long.parseLong(userIdFromToken);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        User user = userMapper.selectOne(queryWrapper);
        if (user.getStudentId() != null) {
            return true;
        }
        return false;
    }
}
