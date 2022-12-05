package com.tjsse.jikespace.service;

import com.tjsse.jikespace.utils.Result;

public interface CollectService {
    Boolean isUserCollectPost(Long userId,Long postId);
    Boolean isUserCollectSection(Long userId,Long sectionId);

    Result collectSection(Long userId, Long sectionId);
}
