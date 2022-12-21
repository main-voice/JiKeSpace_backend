package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findTagBySectionId(Long sectionId);

    List<Long> findPostIdByTagId(Long tagId);
}
