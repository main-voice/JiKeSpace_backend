package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.Section;
import com.tjsse.jikespace.entity.SubSection;
import com.tjsse.jikespace.entity.dto.AddSubSectionDTO;
import com.tjsse.jikespace.entity.dto.PostsWithTagDTO;
import com.tjsse.jikespace.entity.dto.RenameSubSectionDTO;
import com.tjsse.jikespace.entity.dto.SectionDataDTO;
import com.tjsse.jikespace.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface SectionService {
    Result getSectionData(Long userId,SectionDataDTO sectionDataDTO);

    Section findSectionById(Long sectionId);
    SubSection findSubSectionById(Long subsectionId);

    Result getPostsByTag(PostsWithTagDTO postsWithTagDTO);

    List<SubSection> findSubSectionBySectionId(Long sectionId);

    Result collectSection(Integer userId);

    Result hotSection(Integer i);

    Result searchSection(String content);

    void updateSectionByCollectCount(Long sectionId, boolean b);

    void updateSectionByPostCount(Long sectionId, boolean b);

    Result getUserSections(Long userId);

    Result addSubSection(AddSubSectionDTO addSubSectionDTO);

    Result createSection(Long userId, String sectionName, String s, String sectionIntro, String[] subsection);

    Result deleteSubSection(Long userId, Integer subsectionId);

    Result renameSubSection(RenameSubSectionDTO renameSubSectionDTO);

    Result changeSectionAvatar(Long userId, Long sectionId, String avatar);
}
