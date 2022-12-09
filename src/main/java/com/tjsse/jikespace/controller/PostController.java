package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.entity.dto.*;
import com.tjsse.jikespace.service.CollectService;
import com.tjsse.jikespace.service.CommentService;
import com.tjsse.jikespace.service.PostService;
import com.tjsse.jikespace.utils.JKCode;

import com.tjsse.jikespace.service.ReplyService;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * post的控制类
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 20:08
 * @since JDK18
 */
@RestController
@RequestMapping("post/")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private CollectService collectService;

    @GetMapping("post_data/")
    public Result getPostData(@RequestHeader("JK-Token") String jk_token, @RequestBody PostDataDTO postDataDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return postService.getPostData(userId,postDataDTO);
    }

    @PostMapping("collect_post/")
    public Result collectPost(@RequestHeader("JK-Token") String jk_token, @RequestBody CollectPostDTO collectPostDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return collectService.collectPost(userId,collectPostDTO);
    }

    @PostMapping("reply_on_post")
    public Result replyOnPost(@RequestHeader("JK-Token") String jk_token, @RequestBody ReplyOnPostDTO replyOnPostDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        if(replyOnPostDTO.getPostId()==null||replyOnPostDTO.getContent()==null){
            return Result.fail(JKCode.PARAMS_ERROR.getCode(),JKCode.PARAMS_ERROR.getMsg(),null);
        }
        return commentService.replyOnPost(userId,replyOnPostDTO);
    }

    @PostMapping("reply_on_comment/")
    public Result replyOnComment(@RequestHeader("JK-Token") String jk_token, @RequestBody ReplyOnCommentDTO replyOnCommentDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        if(replyOnCommentDTO.getCommentId()==null||replyOnCommentDTO.getContent()==null){
            return Result.fail(JKCode.PARAMS_ERROR.getCode(),JKCode.PARAMS_ERROR.getMsg(),null);
        }
        return replyService.replyOnComment(userId,replyOnCommentDTO);
    }

    @PostMapping("reply_on_reply/")
    public Result replyOnReply(@RequestHeader("JK-Token") String jk_token, @RequestBody ReplyOnReplyDTO replyOnReplyDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        if(replyOnReplyDTO.getReplyId()==null||replyOnReplyDTO.getContent()==null){
            return Result.fail(JKCode.PARAMS_ERROR.getCode(),JKCode.PARAMS_ERROR.getMsg(),null);
        }
        return replyService.replyOnReply(userId,replyOnReplyDTO);
    }

    @DeleteMapping("delete_comment/")
    public Result deleteComment(@RequestHeader("JK-Token") String jk_token, @RequestBody Map<String ,String> map){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        Long commentId = Long.valueOf(map.get("commentId"));
        if(commentId==null){
            return Result.fail(JKCode.PARAMS_ERROR.getCode(),JKCode.PARAMS_ERROR.getMsg(),null);
        }
        return commentService.deleteComment(userId,commentId);
    }

    @DeleteMapping("delete_reply")
    public Result deleteReply(@RequestHeader("JK-Token") String jk_token, @RequestBody Map<String ,String> map){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        Long replyId = Long.valueOf(map.get("replyId"));
        if(replyId==null){
            return Result.fail(JKCode.PARAMS_ERROR.getCode(),JKCode.PARAMS_ERROR.getMsg(),null);
        }
        return replyService.deleteReply(userId,replyId);
    }


}
