package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.vo.PostDataVO;

import java.util.List;

public interface PostService {
    List<PostDataVO> findPostBySectionIdWithPage(Long sectionId,int curPage,int limit);
    List<PostDataVO> findPostBySectionIdAndTagId(Long sectionId,Long tagId,int curPage,int limit);
}
