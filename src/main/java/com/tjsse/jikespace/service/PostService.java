package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.dto.PostDataDTO;
import com.tjsse.jikespace.entity.vo.PostDataVO;
import com.tjsse.jikespace.utils.Result;

import java.util.List;

public interface PostService {
    List<PostDataVO> findPostBySectionIdWithPage(Long sectionId,int curPage,int limit);
    List<PostDataVO> findPostBySectionIdAndSubSectionId(Long sectionId,Long subsectionId,int curPage,int limit);

    void insertPostBody(Long id, String content);

    Result getPostData(Long userId, PostDataDTO postDataDTO);
}
