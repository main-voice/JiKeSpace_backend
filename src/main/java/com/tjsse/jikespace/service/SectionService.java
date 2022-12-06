package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.Section;
import com.tjsse.jikespace.entity.SubSection;
import com.tjsse.jikespace.entity.dto.PostsWithTagDTO;
import com.tjsse.jikespace.entity.dto.SectionDataDTO;
import com.tjsse.jikespace.utils.Result;

import java.util.List;

public interface SectionService {
    Result getSectionData(Long userId,SectionDataDTO sectionDataDTO);

    Section findSectionById(Long sectionId);
    SubSection findSubSectionById(Long subsectionId);

    Result getPostsByTag(PostsWithTagDTO postsWithTagDTO);

    List<SubSection> findSubSectionBySectionId(Long sectionId);

    Result collectSection(Integer userId);

    Result hotSection(Integer i);

    Result searchSection(String content);
}
