package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjsse.jikespace.entity.Post;
import com.tjsse.jikespace.entity.PostAndBody;
import com.tjsse.jikespace.entity.dto.PostDataDTO;
import com.tjsse.jikespace.entity.vo.PostDataVO;
import com.tjsse.jikespace.entity.vo.PostVO;
import com.tjsse.jikespace.mapper.PostAndBodyMapper;
import com.tjsse.jikespace.mapper.PostMapper;
import com.tjsse.jikespace.service.*;
import com.tjsse.jikespace.utils.Result;
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
    private PostAndBodyMapper postAndBodyMapper;
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
        queryWrapper.eq(Post::getDeleted,false);
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
        queryWrapper.eq(Post::getDeleted,false);
        Page<Post> postPage = postMapper.selectPage(page,queryWrapper);
        List<PostDataVO> postDataVOList = copyList(postPage.getRecords());
        return postDataVOList;
    }

    @Override
    public void insertPostBody(Long id, String content) {
        PostAndBody postAndBody = new PostAndBody();
        postAndBody.setPostId(id);
        postAndBody.setContent(content);
        postAndBodyMapper.insert(postAndBody);
    }

    @Override
    public Result getPostData(Long userId, PostDataDTO postDataDTO) {
        Long postId = postDataDTO.getPostId();
        Integer offset = postDataDTO.getOffset();
        Integer limit = postDataDTO.getLimit();

        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getId,postId);
        queryWrapper.last("limit 1");
        Post post = postMapper.selectOne(queryWrapper);

        PostVO postVO = new PostVO();
        postVO.setTitle(post.getTitle());
        postVO.setIsCollected(collectService.isUserCollectPost(userId,postId));
        postVO.setSectionId(post.getSectionId());
        postVO.setSectionName(sectionService.findSectionById(post.getSectionId()).getSectionName());
        postVO.setSubsectionId(post.getSubsectionId());
        postVO.setSubsectionName(sectionService.findSubSectionById(post.getSubsectionId()).getName());
        postVO.setCommentCounts(post.getCommentCounts());
        postVO.setCommentVOList(commentService.findCommentVOsByPostIdWithPage(userId,postId,offset,limit));
        return Result.success(20000,postVO);
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
