package com.tjsse.jikespace.service;

public interface CollectService {
    Boolean isUserCollectPost(Long userId,Long postId);
    Boolean isUserCollectSection(Long userId,Long sectionId);
}
