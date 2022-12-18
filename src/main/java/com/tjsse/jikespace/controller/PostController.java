package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.entity.dto.*;
import com.tjsse.jikespace.entity.vo.PostDataVO;
import com.tjsse.jikespace.service.CollectService;
import com.tjsse.jikespace.service.CommentService;
import com.tjsse.jikespace.service.PostService;
import com.tjsse.jikespace.utils.JKCode;

import com.tjsse.jikespace.service.ReplyService;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.OssService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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
    private OssService ossService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private CollectService collectService;

    @GetMapping("post_data")
    public Result getPostData(@RequestHeader("JK-Token") String jk_token,Integer id,Integer offset,Integer limit){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        PostDataDTO postDataDTO = new PostDataDTO((long)id,offset,limit);
        return postService.getPostData(userId,postDataDTO);
    }

    @PostMapping("collect_post")
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

    @PostMapping("reply_on_comment")
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

    @PostMapping("reply_on_reply")
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

    @DeleteMapping("comment")
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

    @DeleteMapping("reply")
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

    @PostMapping("upload_picture")
    public Result uploadPicture(@RequestHeader("JK-Token") String jk_token,@RequestParam("image") MultipartFile picture){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.valueOf(userIdStr);

        String s = ossService.uploadFile(picture);
        Map<String,String> map = new HashMap<>();
        map.put("url",s);

        return Result.success(20000,"okk",map);
    }


}
