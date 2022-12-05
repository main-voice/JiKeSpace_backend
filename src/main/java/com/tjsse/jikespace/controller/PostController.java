package com.tjsse.jikespace.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.Post;
import com.tjsse.jikespace.entity.dto.PostDataDTO;
import com.tjsse.jikespace.entity.dto.SectionDataDTO;
import com.tjsse.jikespace.entity.vo.PostVO;
import com.tjsse.jikespace.mapper.PostMapper;
import com.tjsse.jikespace.service.CollectService;
import com.tjsse.jikespace.service.CommentService;
import com.tjsse.jikespace.service.PostService;
import com.tjsse.jikespace.service.SectionService;
import com.tjsse.jikespace.utils.JwtUtil;
import com.tjsse.jikespace.utils.Result;
import com.tjsse.jikespace.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * post的控制类
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 20:08
 * @since JDK18
 */
@RestController
@RequestMapping("post/")
public class PostController {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("post_data")
    public Result getPostData(@RequestHeader("JK-Token") String jk_token, @RequestBody PostDataDTO postDataDTO){
        String userIdStr = JwtUtil.getUserIdFromToken(jk_token);
        if (userIdStr == null) {
            return Result.fail(StatusCode.OTHER_ERROR.getCode(), "从token中解析到到userId为空", null);
        }
        Long userId = Long.parseLong(userIdStr);
        return postService.getPostData(userId,postDataDTO);
    }
}
