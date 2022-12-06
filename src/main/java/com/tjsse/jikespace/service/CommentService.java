package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.Comment;
import com.tjsse.jikespace.entity.dto.ReplyOnPostDTO;
import com.tjsse.jikespace.entity.vo.CommentVO;
import com.tjsse.jikespace.utils.Result;

import java.util.List;

public interface CommentService {

    List<CommentVO> findCommentVOsByPostIdWithPage(Long usrId,Long postId,Integer offset,Integer limit);

    Result replyOnPost(Long userId, ReplyOnPostDTO replyOnPostDTO);

    Result deleteComment(Long userId, Long commentId);
}
