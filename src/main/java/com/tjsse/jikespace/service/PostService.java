package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.Post;
import com.tjsse.jikespace.entity.dto.PostDataDTO;
import com.tjsse.jikespace.entity.dto.PostPublishDTO;
import com.tjsse.jikespace.entity.vo.FolderPostVO;
import com.tjsse.jikespace.entity.vo.PostDataVO;
import com.tjsse.jikespace.utils.Result;

import java.util.List;

public interface PostService {
    List<PostDataVO> findPostBySectionIdWithPage(Long sectionId,int curPage,int limit);
    List<PostDataVO> findPostBySectionIdAndSubSectionId(Long sectionId,Long subsectionId,int curPage,int limit);


    Result getPostData(Long userId, PostDataDTO postDataDTO);

    Result hotPost();

    Result getNews();

    Result publishPost(Long userId, PostPublishDTO postPublishDTO);

    void updatePostByCommentCount(Long postId, boolean b);

    List<FolderPostVO> findPostsByFolderIdWithPage(Long folderId,Integer curPage,Integer limit);

    Result findPostsByUserIdWithPage(Long userId, String type, Integer curPage, Integer limit);

    Result deleteMyPost(Long postId, Long userId);

    List<Long> findPostIdsByUserId(Long userId);
}
