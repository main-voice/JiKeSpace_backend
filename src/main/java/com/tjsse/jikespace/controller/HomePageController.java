package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.service.PostService;
import com.tjsse.jikespace.service.SectionService;
import com.tjsse.jikespace.utils.JKCode;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 首页管理
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/6 20:46
 * @since JDK18
 */
@RestController
@RequestMapping("homepage/")
public class HomePageController {
    @Autowired
    private SectionService sectionService;
    @Autowired
    private PostService postService;

    @GetMapping("collect_section")
    public Result collectSection(@RequestHeader("JK-Token") String jk_token){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(JKCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Integer userId = Integer.parseInt(userIdStr);
        return sectionService.collectSection(userId);
    }

    @GetMapping("hot_section")
    public Result hotSection(){
        return sectionService.hotSection(5);
    }

    @GetMapping("search_section")
    public Result searchSection(String searchContent){
        return sectionService.searchSection(searchContent);
    }

    @GetMapping("hot_post")
    public Result hotPost(){
        return postService.hotPost();
    }

    @GetMapping("news")
    public Result getNews(){
        return postService.getNews();
    }
}
