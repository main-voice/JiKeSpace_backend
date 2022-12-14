package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.CollectAndPost;
import com.tjsse.jikespace.entity.CollectAndSection;
import com.tjsse.jikespace.entity.dto.CollectPostDTO;
import com.tjsse.jikespace.mapper.CollectAndPostMapper;
import com.tjsse.jikespace.mapper.CollectAndSectionMapper;
import com.tjsse.jikespace.service.CollectService;
import com.tjsse.jikespace.service.SectionService;
import com.tjsse.jikespace.utils.JKCode;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏功能的实现类
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 15:21
 * @since JDK18
 */
@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private SectionService sectionService;
    @Autowired
    private CollectAndPostMapper collectAndPostMapper;
    @Autowired
    private CollectAndSectionMapper collectAndSectionMapper;



    @Override
    public Boolean isUserCollectPost(Long userId, Long postId) {
        LambdaQueryWrapper<CollectAndPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectAndPost::getId,postId);
        queryWrapper.eq(CollectAndPost::getUserId,userId);
        queryWrapper.last("limit 1");
        if(collectAndPostMapper.selectOne(queryWrapper)==null)
            return false;
        else
            return true;
    }

    @Override
    public Boolean isUserCollectSection(Long userId, Long sectionId) {
        LambdaQueryWrapper<CollectAndSection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectAndSection::getSectionId,sectionId);
        queryWrapper.eq(CollectAndSection::getUserId,userId);
        queryWrapper.last("limit 1");
        if(collectAndSectionMapper.selectOne(queryWrapper)==null)
            return false;
        else
            return true;
    }

    @Override
    public Result collectSection(Long userId, Long sectionId) {
        Boolean isCollected = this.isUserCollectSection(userId,sectionId);
        LambdaQueryWrapper<CollectAndSection> queryWrapper= new LambdaQueryWrapper<>();
        if(isCollected){
            queryWrapper.eq(CollectAndSection::getUserId,userId);
            queryWrapper.eq(CollectAndSection::getSectionId,sectionId);
            queryWrapper.last("limit 1");
            this.collectAndSectionMapper.delete(queryWrapper);

            sectionService.updateSectionByCollectCount(sectionId,false);
            return Result.success(JKCode.SUCCESS.getCode(),"已取消收藏",null);
        }
        else {
            CollectAndSection collectAndSection = new CollectAndSection();
            collectAndSection.setSectionId(sectionId);
            collectAndSection.setUserId(userId);
            this.collectAndSectionMapper.insert(collectAndSection);

            sectionService.updateSectionByCollectCount(sectionId,true);
            return Result.success(20000,"收藏成功",null);
        }
    }

    @Override
    public Result collectPost(Long userId, CollectPostDTO collectPostDTO) {
        Long postId = collectPostDTO.getId();
        Long folderId = collectPostDTO.getFolderId();
        if(postId==null||folderId==null){
            return Result.fail(JKCode.PARAMS_ERROR.getCode(),JKCode.PARAMS_ERROR.getMsg());
        }


        LambdaQueryWrapper<CollectAndPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectAndPost::getPostId,postId);
        queryWrapper.eq(CollectAndPost::getUserId,userId);
        queryWrapper.eq(CollectAndPost::getFolderId,folderId);
        queryWrapper.last("limit 1");
        CollectAndPost collectAndPost1 = collectAndPostMapper.selectOne(queryWrapper);

        if(collectAndPost1 == null){
            CollectAndPost collectAndPost =new CollectAndPost();
            collectAndPost.setPostId(postId);
            collectAndPost.setUserId(userId);
            collectAndPost.setFolderId(folderId);
            collectAndPostMapper.insert(collectAndPost);
            return Result.success(20000,"收藏成功",null);
        }
        else{
            collectAndPostMapper.delete(queryWrapper);
            return Result.success(20000,"取消收藏成功",null);
        }
    }

    @Override
    public List<Long> findPostIdsByFolderId(Long folderId) {
        LambdaQueryWrapper<CollectAndPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectAndPost::getFolderId,folderId);
        List<CollectAndPost> collectAndPosts = collectAndPostMapper.selectList(queryWrapper);
        if(collectAndPosts.size()==0){
            return null;
        }
        List<Long> postIds = new ArrayList<>();
        for (CollectAndPost collectAndPost :
                collectAndPosts) {
            postIds.add(collectAndPost.getPostId());
        }
        return postIds;
    }

    @Override
    public void deleteCollectPostByFolderId(Long folderId) {
        LambdaQueryWrapper<CollectAndPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectAndPost::getFolderId,folderId);
        collectAndPostMapper.delete(queryWrapper);
    }
}
