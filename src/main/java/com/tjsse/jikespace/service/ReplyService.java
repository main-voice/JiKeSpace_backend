package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.dto.ReplyOnCommentDTO;
import com.tjsse.jikespace.entity.dto.ReplyOnReplyDTO;
import com.tjsse.jikespace.utils.Result;

public interface ReplyService {
    Result replyOnComment(Long userId, ReplyOnCommentDTO replyOnCommentDTO);

    Result replyOnReply(Long userId, ReplyOnReplyDTO replyOnReplyDTO);

    Result deleteReply(Long userId, Long replyId);
}
