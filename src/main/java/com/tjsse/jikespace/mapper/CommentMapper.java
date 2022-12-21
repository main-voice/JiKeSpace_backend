package com.tjsse.jikespace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tjsse.jikespace.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    List<Comment> findCommentsByPostId(Long postId);
}
