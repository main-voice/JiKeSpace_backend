package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.CollectPost;
import com.tjsse.jikespace.entity.CollectSection;
import com.tjsse.jikespace.mapper.CollectPostMapper;
import com.tjsse.jikespace.mapper.CollectSectionMapper;
import com.tjsse.jikespace.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private CollectPostMapper collectPostMapper;
    @Autowired
    private CollectSectionMapper collectSectionMapper;


    @Override
    public Boolean isUserCollectPost(Long userId, Long postId) {
        LambdaQueryWrapper<CollectPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectPost::getId,postId);
        queryWrapper.eq(CollectPost::getUserId,userId);
        queryWrapper.last("limit 1");
        if(collectPostMapper.selectOne(queryWrapper)==null)
            return false;
        else
            return true;
    }

    @Override
    public Boolean isUserCollectSection(Long userId, Long sectionId) {
        LambdaQueryWrapper<CollectSection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectSection::getId,sectionId);
        queryWrapper.eq(CollectSection::getUserId,userId);
        queryWrapper.last("limit 1");
        if(collectSectionMapper.selectOne(queryWrapper)==null)
            return false;
        else
            return true;
    }
}
