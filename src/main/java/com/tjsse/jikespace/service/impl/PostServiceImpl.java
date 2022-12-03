package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjsse.jikespace.entity.Post;
import com.tjsse.jikespace.entity.vo.PostDataVO;
import com.tjsse.jikespace.mapper.PostMapper;
import com.tjsse.jikespace.service.PostService;
import com.tjsse.jikespace.service.TagService;
import com.tjsse.jikespace.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * post服务类的实现类
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 16:17
 * @since JDK18
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;
    @Override
    public List<PostDataVO> findPostBySectionIdWithPage(Long sectionId,int curPage,int limit) {
        Page<Post> page = new Page<>(curPage,limit);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getSectionId,sectionId);
        queryWrapper.eq(Post::getDeleted,false);
        Page<Post> postPage = postMapper.selectPage(page,queryWrapper);
        List<PostDataVO> postDataVOList = copyList(postPage.getRecords());
        return postDataVOList;
    }

    @Override
    public List<PostDataVO> findPostBySectionIdAndTagId(Long sectionId, Long tagId, int curPage, int limit) {
        List<Long> postIdList = tagService.findPostIdByTagId(tagId);
        Long[] list = postIdList.toArray(new Long[postIdList.size()]);
        if(postIdList.size()==0)
            return null;
        Page<Post> page = new Page<>(curPage,limit);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getSectionId,sectionId);
        queryWrapper.in(Post::getId,postIdList);
        queryWrapper.eq(Post::getDeleted,false);
        Page<Post> postPage = postMapper.selectPage(page,queryWrapper);
        List<PostDataVO> postDataVOList = copyList(postPage.getRecords());
        return postDataVOList;
    }


    private List<PostDataVO> copyList(List<Post> postList) {
        List<PostDataVO> voList = new ArrayList<>();
        for(Post post : postList){
                voList.add(copy(post));
        }
        return voList;
    }

    private PostDataVO copy(Post post) {
        PostDataVO postDataVO = new PostDataVO();
        BeanUtils.copyProperties(post,postDataVO);
        postDataVO.setPoster(userService.findUserById(post.getAuthorId()).getUsername());
        return postDataVO;
    }
}
