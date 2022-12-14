package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.dto.ReplyOnCommentDTO;
import com.tjsse.jikespace.entity.dto.ReplyOnReplyDTO;
import com.tjsse.jikespace.entity.vo.ReplyVO;
import com.tjsse.jikespace.utils.Result;

import java.util.List;

public interface ReplyService {
    Result replyOnComment(Long userId, ReplyOnCommentDTO replyOnCommentDTO);

    Result replyOnReply(Long userId, ReplyOnReplyDTO replyOnReplyDTO);

    Result deleteReply(Long userId, Long replyId);

    List<ReplyVO> findReplysByCommentId(Long id,Long userId);
}
