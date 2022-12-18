package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjsse.jikespace.entity.*;
import com.tjsse.jikespace.entity.dto.ReplyOnPostDTO;
import com.tjsse.jikespace.entity.vo.CommentVO;
import com.tjsse.jikespace.entity.vo.ReplyVO;
import com.tjsse.jikespace.mapper.CommentAndBodyMapper;
import com.tjsse.jikespace.mapper.CommentMapper;
import com.tjsse.jikespace.mapper.PostAndCommentMapper;
import com.tjsse.jikespace.mapper.PostMapper;
import com.tjsse.jikespace.service.CommentService;
import com.tjsse.jikespace.service.PostService;
import com.tjsse.jikespace.service.ReplyService;
import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 评论服务类的实现类
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/5 15:59
 * @since JDK18
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentAndBodyMapper commentAndBodyMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostAndCommentMapper postAndCommentMapper;
    @Autowired
    private ReplyService replyService;

    @Override
    public List<CommentVO> findCommentVOsByPostIdWithPage(Long userId,Long postId,Integer offset,Integer limit) {
        Page<Comment> commentPage = new Page<>(offset,limit);
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getPostId,postId);
        queryWrapper.eq(Comment::getIsDeleted,false);
        Page<Comment> commentPage1 = commentMapper.selectPage(commentPage,queryWrapper);
        List<Comment> records = commentPage1.getRecords();
        List<CommentVO> commentVOList = this.copyList(records,userId);
/*        for (CommentVO commentVO :
                commentVOList) {
            String userName = userService.findUserById(userId).getUsername();
            if(commentVO.getAuthor()==null||userName == null){
                commentVO.setAbleToDelete(false);
            }
            else{
                commentVO.setAbleToDelete(commentVO.getAuthor() == userName);
            }
        }*/
        return commentVOList;
    }

    @Override
    public Result replyOnPost(Long userId, ReplyOnPostDTO replyOnPostDTO) {
        Long postId = replyOnPostDTO.getPostId();

        postService.updatePostByCommentCount(postId,true);

        String content = replyOnPostDTO.getContent();
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthorId(userId);
        comment.setIsDeleted(false);
        comment.setUpdateTime(LocalDateTime.now());
        commentMapper.insert(comment);
        CommentAndBody commentAndBody = new CommentAndBody();
        commentAndBody.setCommentId(comment.getId());
        commentAndBody.setContent(content);
        commentAndBodyMapper.insert(commentAndBody);

        comment.setBodyId(commentAndBody.getId());
        commentMapper.updateById(comment);

        PostAndComment postAndComment =new PostAndComment();
        postAndComment.setPostId(postId);
        postAndComment.setCommentId(comment.getId());
        postAndCommentMapper.insert(postAndComment);

        return Result.success(20000,"操作成功",null);
    }

    @Override
    public Result deleteComment(Long userId, Long commentId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getIsDeleted,false);
        queryWrapper.eq(Comment::getId,commentId);
        queryWrapper.eq(Comment::getAuthorId,userId);
        queryWrapper.last("limit 1");
        Comment comment = commentMapper.selectOne(queryWrapper);
        if(comment==null){
            return Result.fail(-1,"参数有误",null);
        }
        comment.setIsDeleted(true);
        commentMapper.updateById(comment);

        LambdaQueryWrapper<PostAndComment> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(PostAndComment::getCommentId,commentId);
        queryWrapper2.last("limit 1");
        postAndCommentMapper.delete(queryWrapper2);

        postService.updatePostByCommentCount(comment.getPostId(),false);
        return Result.success(20000,"删除成功",null);
    }

    private List<CommentVO> copyList(List<Comment> commentList,Long userId) {
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment :
                commentList) {
            commentVOList.add(copy(comment,userId));
        }
        return commentVOList;
    }

    private CommentVO copy(Comment comment,Long userId) {
        CommentVO commentVO = new CommentVO();
        User user = userService.findUserById(comment.getAuthorId());
        commentVO.setCommentId(comment.getId());
        commentVO.setAuthor(user.getUsername());
        commentVO.setAvatar(user.getAvatar());
        commentVO.setUpdateTime(comment.getUpdateTime());
        commentVO.setContent(this.findContentByBodyId(comment.getBodyId()));
        commentVO.setAbleToDelete(Objects.equals(comment.getAuthorId(), userId));
        List<ReplyVO> replyVOList = new ArrayList<>();
        replyVOList = replyService.findReplysByCommentId(comment.getId(),userId);
        commentVO.setReplyVOList(replyVOList);
        return commentVO;
    }

    private String findContentByBodyId(Long bodyId) {
        LambdaQueryWrapper<CommentAndBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommentAndBody::getId,bodyId);
        queryWrapper.last("limit 1");
        String content = commentAndBodyMapper.selectOne(queryWrapper).getContent();
        return content;
    }
}
