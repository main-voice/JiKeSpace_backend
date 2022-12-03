package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.PostTag;
import com.tjsse.jikespace.entity.Tag;
import com.tjsse.jikespace.mapper.PostTagMapper;
import com.tjsse.jikespace.mapper.TagMapper;
import com.tjsse.jikespace.service.TagService;
import com.tjsse.jikespace.utils.Result;
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
    private PostTagMapper postTagMapper;
    @Override
    public List<Tag> findTagBySectionId(Long sectionId) {
        List<Tag> tagList = tagMapper.findTagBySectionId(sectionId);
        return tagList;
    }

    @Override
    public List<Long> findPostIdByTagId(Long tagId) {
        LambdaQueryWrapper<PostTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostTag::getTagId,tagId);
        List<PostTag> postTags = postTagMapper.selectList(queryWrapper);
        List<Long> postIdList = new ArrayList<>();
        for(PostTag postTag:postTags){
            postIdList.add(postTag.getPostId());
        }
        return postIdList;
    }
}
