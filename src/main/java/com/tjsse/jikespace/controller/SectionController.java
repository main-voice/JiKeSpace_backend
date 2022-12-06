package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.entity.CollectAndSection;
import com.tjsse.jikespace.entity.Post;
import com.tjsse.jikespace.entity.Section;
import com.tjsse.jikespace.entity.dto.PostPublishDTO;
import com.tjsse.jikespace.entity.dto.PostsWithTagDTO;
import com.tjsse.jikespace.entity.dto.SectionDataDTO;
import com.tjsse.jikespace.mapper.PostMapper;
import com.tjsse.jikespace.service.CollectService;
import com.tjsse.jikespace.service.PostService;
import com.tjsse.jikespace.service.SectionService;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.OssService;
import com.tjsse.jikespace.utils.Result;
import com.tjsse.jikespace.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
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
    private OssService ossService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostMapper postMapper;

    @GetMapping("get_section_data/")
    public Result getSectionData(@RequestHeader("JK-Token") String jk_token,@RequestBody SectionDataDTO sectionDataDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(StatusCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.valueOf(userIdStr);
        return sectionService.getSectionData(userId,sectionDataDTO);
    }

    @GetMapping("get_posts_by_tag")
    public Result getPostsByTag(@RequestBody PostsWithTagDTO postsWithTagDTO){
        return sectionService.getPostsByTag(postsWithTagDTO);
    }

    @PostMapping ("collect_section/")
    public Result collectSection(@RequestHeader("JK-Token") String jk_token, @RequestBody Map<String, String> map){
        Integer sectionId = Integer.valueOf(map.get("sectionId"));
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(StatusCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Integer userId = Integer.parseInt(userIdStr);
        return collectService.collectSection((long)userId,(long)sectionId);
    }

    @PostMapping("publish_post/")
    public Result publishPost(@RequestHeader("JK-Token") String jk_token, @RequestBody PostPublishDTO postPublishDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(StatusCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return postService.publishPost(userId,postPublishDTO);
    }
}
