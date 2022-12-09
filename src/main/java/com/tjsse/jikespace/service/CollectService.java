package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.dto.CollectPostDTO;
import com.tjsse.jikespace.entity.dto.PostPublishDTO;
import com.tjsse.jikespace.utils.Result;

import java.util.List;

public interface CollectService {
    Boolean isUserCollectPost(Long userId,Long postId);
    Boolean isUserCollectSection(Long userId,Long sectionId);

    Result collectSection(Long userId, Long sectionId);

    Result collectPost(Long userId, CollectPostDTO collectPostDTO);

    List<Long> findPostIdsByFolderId(Long folderId);

    void deleteCollectPostByFolderId(Long folderId);
}
