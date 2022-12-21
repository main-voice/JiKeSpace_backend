package com.tjsse.jikespace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tjsse.jikespace.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> findTagBySectionId(Long sectionId);
}
