package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.CommentAndReply;
import com.tjsse.jikespace.entity.Reply;
import com.tjsse.jikespace.entity.dto.ReplyOnCommentDTO;
import com.tjsse.jikespace.entity.dto.ReplyOnReplyDTO;
import com.tjsse.jikespace.mapper.CommentAndReplyMapper;
import com.tjsse.jikespace.mapper.ReplyMapper;
import com.tjsse.jikespace.service.ReplyService;
import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/6 16:02
 * @since JDK18
 */
@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private UserService userService;
    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private CommentAndReplyMapper commentAndReplyMapper;
    @Override
    public Result replyOnComment(Long userId, ReplyOnCommentDTO replyOnCommentDTO) {
        Long commentId = replyOnCommentDTO.getCommentId();
        String content = replyOnCommentDTO.getContent();
        Reply reply = new Reply();
        reply.setCommentId(commentId);
        reply.setContent(content);
        reply.setType("1");
        reply.setAuthorId(userId);
        reply.setTo_uid(userService.findUserIdByCommentId(commentId));
        reply.setUpdateTime(LocalDateTime.now());
        replyMapper.insert(reply);

        CommentAndReply commentAndReply = new CommentAndReply();
        commentAndReply.setReplyId(reply.getId());
        commentAndReply.setCommentId(commentId);
        commentAndReplyMapper.insert(commentAndReply);

        return Result.success(20000,"操作成功",null);
    }

    @Override
    public Result replyOnReply(Long userId, ReplyOnReplyDTO replyOnReplyDTO) {
        Long replyId = replyOnReplyDTO.getReplyId();
        String content = replyOnReplyDTO.getContent();

        LambdaQueryWrapper<Reply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reply::getId,replyId);
        queryWrapper.last("limit 1");
        Reply reply1 = replyMapper.selectOne(queryWrapper);
        if(reply1==null){
            return Result.fail(-1,"回复的帖子不存在",null);
        }

        Reply reply = new Reply();
        reply.setParentId(replyId);
        reply.setContent(content);
        reply.setType("2");
        reply.setAuthorId(userId);
        reply.setTo_uid(this.findUserIdByReplyId(replyId));
        reply.setUpdateTime(LocalDateTime.now());
        replyMapper.insert(reply);

        LambdaQueryWrapper<CommentAndReply> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(CommentAndReply::getReplyId,replyId);
        queryWrapper1.last("limit 1");
        CommentAndReply commentAndReply = commentAndReplyMapper.selectOne(queryWrapper1);

        CommentAndReply commentAndReply1 = new CommentAndReply();
        commentAndReply1.setReplyId(reply.getId());
        commentAndReply1.setCommentId(commentAndReply.getCommentId());
        commentAndReplyMapper.insert(commentAndReply1);



        return Result.success(20000,"操作成功",null);
    }

    @Override
    public Result deleteReply(Long userId, Long replyId) {
        LambdaQueryWrapper<Reply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reply::getId,replyId);
        queryWrapper.last("limit 1");
        Reply reply = replyMapper.selectOne(queryWrapper);
        if(reply==null){
            return Result.fail(-1,"参数有误",null);
        }
        replyMapper.delete(queryWrapper);

        LambdaQueryWrapper<CommentAndReply> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(CommentAndReply::getReplyId,replyId);
        queryWrapper1.last("limit 1");
        commentAndReplyMapper.delete(queryWrapper1);

        return Result.success(20000,"操作成功",null);
    }

    private Long findUserIdByReplyId(Long replyId) {
        LambdaQueryWrapper<Reply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reply::getId,replyId);
        queryWrapper.last("limit 1");
        Reply reply = replyMapper.selectOne(queryWrapper);
        return reply.getAuthorId();
    }
}
