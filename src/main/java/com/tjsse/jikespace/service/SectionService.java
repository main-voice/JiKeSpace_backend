package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.Section;
import com.tjsse.jikespace.entity.dto.PostsWithTagDTO;
import com.tjsse.jikespace.entity.dto.SectionDataDTO;
import com.tjsse.jikespace.utils.Result;

public interface SectionService {
    Result getSectionData(SectionDataDTO sectionDataDTO);

    Section findSectionById(Long sectionId);

    Result getPostsByTag(PostsWithTagDTO postsWithTagDTO);
}
