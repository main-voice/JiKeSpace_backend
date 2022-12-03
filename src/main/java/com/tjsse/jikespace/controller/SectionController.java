package com.tjsse.jikespace.controller;

import com.tjsse.jikespace.entity.Section;
import com.tjsse.jikespace.entity.dto.PostsWithTagDTO;
import com.tjsse.jikespace.entity.dto.SectionDataDTO;
import com.tjsse.jikespace.service.SectionService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("get_section_data/")
    public Result getSectionData(@RequestBody SectionDataDTO sectionDataDTO){
        return sectionService.getSectionData(sectionDataDTO);
    }

    @GetMapping("get_posts_by_tag")
    public Result getPostsByTag(@RequestBody PostsWithTagDTO postsWithTagDTO){
        return sectionService.getPostsByTag(postsWithTagDTO);
    }

}
