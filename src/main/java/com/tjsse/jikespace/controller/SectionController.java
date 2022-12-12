package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.entity.dto.PostPublishDTO;
import com.tjsse.jikespace.entity.dto.PostsWithTagDTO;
import com.tjsse.jikespace.entity.dto.SectionDataDTO;
import com.tjsse.jikespace.mapper.PostMapper;
import com.tjsse.jikespace.service.CollectService;
import com.tjsse.jikespace.service.PostService;
import com.tjsse.jikespace.service.SectionService;
import com.tjsse.jikespace.utils.JKCode;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.OssService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 版块的控制类
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 11:40
 * @since JDK18
 */
@RestController
@RequestMapping("section/")
public class SectionController {
    @Autowired
    private SectionService sectionService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private PostService postService;

    @GetMapping("get_section_data")
    public Result getSectionData(@RequestHeader("JK-Token") String jk_token,Integer sectionId,Integer curPage,Integer limit){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.valueOf(userIdStr);
        SectionDataDTO sectionDataDTO = new SectionDataDTO((long)sectionId,curPage,limit);
        return sectionService.getSectionData(userId,sectionDataDTO);
    }

    @GetMapping("get_posts_by_subsection")
    public Result getPostsByTag(Integer sectionId,Integer subsectionId,Integer curPage,Integer limit){
        PostsWithTagDTO postsWithTagDTO = new PostsWithTagDTO((long)sectionId,(long)subsectionId,curPage,limit);
        return sectionService.getPostsByTag(postsWithTagDTO);
    }

    @PostMapping ("collect_section")
    public Result collectSection(@RequestHeader("JK-Token") String jk_token, @RequestBody Map<String, String> map){
        Long sectionId = Long.valueOf(map.get("sectionId"));
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.valueOf(userIdStr);
        return collectService.collectSection(userId,sectionId);
    }

    @PostMapping("publish_post")
    public Result publishPost(@RequestHeader("JK-Token") String jk_token, @RequestBody PostPublishDTO postPublishDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.valueOf(userIdStr);
        return postService.publishPost(userId,postPublishDTO);
    }
}
