package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.Comment;
import com.tjsse.jikespace.entity.vo.CommentVO;

import java.util.List;

public interface CommentService {

    List<CommentVO> findCommentVOsByPostIdWithPage(Long usrId,Long postId,Integer offset,Integer limit);
}
