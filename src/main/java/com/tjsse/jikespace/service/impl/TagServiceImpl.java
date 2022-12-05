package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.PostAndTag;
import com.tjsse.jikespace.entity.Tag;
import com.tjsse.jikespace.mapper.PostAndTagMapper;
import com.tjsse.jikespace.mapper.TagMapper;
import com.tjsse.jikespace.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * tag服务类的实现
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 15:40
 * @since JDK18
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private PostAndTagMapper postTagMapper;
    @Override
    public List<Tag> findTagBySectionId(Long sectionId) {
        List<Tag> tagList = tagMapper.findTagBySectionId(sectionId);
        return tagList;
    }

    @Override
    public List<Long> findPostIdByTagId(Long tagId) {
        LambdaQueryWrapper<PostAndTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostAndTag::getTagId,tagId);
        List<PostAndTag> postAndTags = postTagMapper.selectList(queryWrapper);
        List<Long> postIdList = new ArrayList<>();
        for(PostAndTag postAndTag : postAndTags){
            postIdList.add(postAndTag.getPostId());
        }
        return postIdList;
    }
}
