package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjsse.jikespace.entity.Comment;
import com.tjsse.jikespace.entity.CommentAndBody;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.entity.vo.CommentVO;
import com.tjsse.jikespace.mapper.CommentAndBodyMapper;
import com.tjsse.jikespace.mapper.CommentMapper;
import com.tjsse.jikespace.service.CommentService;
import com.tjsse.jikespace.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<CommentVO> findCommentVOsByPostIdWithPage(Long userId,Long postId,Integer offset,Integer limit) {
        Page<Comment> commentPage = new Page<>(offset,limit);
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getPostId,postId);
        queryWrapper.eq(Comment::getIsDeleted,false);
        Page<Comment> commentPage1 = commentMapper.selectPage(commentPage,queryWrapper);
//        List<Comment> commentList = commentMapper.findCommentsByPostId(postId);
        List<CommentVO> commentVOList = this.copyList(commentPage1.getRecords());
        for (CommentVO commentVO :
                commentVOList) {
            commentVO.setAbleToDelete(commentVO.getAuthorName() == userService.findUserById(userId).getUsername());
        }
        return commentVOList;
    }

    private List<CommentVO> copyList(List<Comment> commentList) {
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment :
                commentList) {
            commentVOList.add(copy(comment));
        }
        return commentVOList;
    }

    private CommentVO copy(Comment comment) {
        CommentVO commentVO = new CommentVO();
        User user = userService.findUserById(comment.getId());
        commentVO.setAuthorName(user.getUsername());
        commentVO.setAvatar(user.getAvatar());
        commentVO.setUpdateTime(comment.getUpdateTime());
        commentVO.setContent(this.findContentByBodyId(comment.getBodyId()));
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
