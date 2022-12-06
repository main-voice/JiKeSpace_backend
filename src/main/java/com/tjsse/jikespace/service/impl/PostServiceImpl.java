package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjsse.jikespace.entity.*;
import com.tjsse.jikespace.entity.dto.PostDataDTO;
import com.tjsse.jikespace.entity.dto.PostPublishDTO;
import com.tjsse.jikespace.entity.vo.PostDataVO;
import com.tjsse.jikespace.entity.vo.PostVO;
import com.tjsse.jikespace.mapper.CollectAndPostMapper;
import com.tjsse.jikespace.mapper.PostAndBodyMapper;
import com.tjsse.jikespace.mapper.PostMapper;
import com.tjsse.jikespace.mapper.SectionMapper;
import com.tjsse.jikespace.service.*;
import com.tjsse.jikespace.utils.Result;
import com.tjsse.jikespace.utils.StatusCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
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
    private PostAndBodyMapper postAndBodyMapper;
    @Autowired
    private CollectAndPostMapper collectAndPostMapper;
    @Autowired
    private SectionMapper sectionMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private CommentService commentService;


    @Override
    public List<PostDataVO> findPostBySectionIdWithPage(Long sectionId,int curPage,int limit) {
        Page<Post> page = new Page<>(curPage,limit);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getSectionId,sectionId);
        queryWrapper.eq(Post::getIsDeleted,false);
        Page<Post> postPage = postMapper.selectPage(page,queryWrapper);
        List<PostDataVO> postDataVOList = copyList(postPage.getRecords());
        return postDataVOList;
    }

    @Override
    public List<PostDataVO> findPostBySectionIdAndSubSectionId(Long sectionId, Long subsectionId, int curPage, int limit) {
        Page<Post> page = new Page<>(curPage,limit);
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getSectionId,sectionId);
        queryWrapper.in(Post::getSubsectionId,subsectionId);
        queryWrapper.eq(Post::getIsDeleted,false);
        Page<Post> postPage = postMapper.selectPage(page,queryWrapper);
        List<PostDataVO> postDataVOList = copyList(postPage.getRecords());
        return postDataVOList;
    }


    @Override
    public Result getPostData(Long userId, PostDataDTO postDataDTO) {
        Long postId = postDataDTO.getId();
        Integer offset = postDataDTO.getOffset();
        Integer limit = postDataDTO.getLimit();

        if(postId==null||offset==null||limit==null){
            return Result.fail(StatusCode.PARAMS_ERROR.getCode(),StatusCode.PARAMS_ERROR.getMsg());
        }

        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getId,postId);
        queryWrapper.last("limit 1");
        Post post = postMapper.selectOne(queryWrapper);

        if(post==null){
            return Result.fail(-1,"该帖子不存在",null);
        }

        PostVO postVO = new PostVO();
        postVO.setTitle(post.getTitle());
        postVO.setIsCollected(collectService.isUserCollectPost(userId,postId));
        postVO.setSectionId(post.getSectionId());
        Section section = sectionService.findSectionById(post.getSectionId());
        if(section.getSectionName()==null){
            postVO.setSectionName(null);
        }
        else {
            postVO.setSectionName(section.getSectionName());
        }

        if(post.getSubsectionId()==null){
            postVO.setSubsectionId(null);
            postVO.setSubsectionName(null);
        }
        else {
            postVO.setSubsectionId(post.getSubsectionId());
            SubSection subSection = sectionService.findSubSectionById(post.getSubsectionId());
            if(subSection==null){
                postVO.setSubsectionName(null);
            }
            else {
                postVO.setSubsectionName(subSection.getName());
            }
        }

        postVO.setCommentCounts(post.getCommentCounts());
        postVO.setCommentVOList(commentService.findCommentVOsByPostIdWithPage(userId,postId,offset,limit));
        return Result.success(20000,postVO);
    }

    @Override
    public Result collectPost(Long userId, Long postId) {
        LambdaQueryWrapper<CollectAndPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectAndPost::getPostId,postId);
        queryWrapper.eq(CollectAndPost::getUserId,userId);
        queryWrapper.last("limit 1");
        CollectAndPost collectAndPost1 = collectAndPostMapper.selectOne(queryWrapper);

        if(collectAndPost1 == null){
            CollectAndPost collectAndPost =new CollectAndPost();
            collectAndPost.setPostId(postId);
            collectAndPost.setUserId(userId);
            collectAndPostMapper.insert(collectAndPost);
            return Result.success(20000,"收藏成功");
        }
        else{
            collectAndPostMapper.delete(queryWrapper);
            return Result.success(20000,"取消收藏成功");
        }
    }

    @Override
    public Result publishPost(Long userId, PostPublishDTO postPublishDTO) {
        Post post = new Post();
        post.setSectionId(postPublishDTO.getSectionId());
        post.setTitle(postPublishDTO.getTitle());
        post.setSubsectionId(postPublishDTO.getSubsectionId());
        post.setAuthorId(userId);
        post.setIsDeleted(false);
        post.setUpdateTime(LocalDateTime.now());
        this.postMapper.insert(post);

        PostAndBody postAndBody = new PostAndBody();
        postAndBody.setPostId(post.getId());
        postAndBody.setContent(postPublishDTO.getContent());
        postAndBodyMapper.insert(postAndBody);

        post.setBodyId(postAndBody.getId());
        postMapper.updateById(post);

        LambdaQueryWrapper<Section> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Section::getId,postPublishDTO.getSectionId());
        queryWrapper.last("limit 1");
        Section section = sectionMapper.selectOne(queryWrapper);
        section.setPostCounts(section.getPostCounts()+1);
        sectionMapper.updateById(section);

        return Result.success(20000,"操作成功",null);
    }

    @Override
    public Result hotPost() {
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Post::getCommentCounts);
        queryWrapper.last("limit 10");
        List<Post> postList = postMapper.selectList(queryWrapper);
        return Result.success(copyList(postList));
    }

    @Override
    public Result getNews() {
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getSectionId,1);
        queryWrapper.orderByDesc(Post::getCommentCounts);
        queryWrapper.last("limit 10");
        List<Post> postList = postMapper.selectList(queryWrapper);
        return Result.success(copyList(postList));
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
