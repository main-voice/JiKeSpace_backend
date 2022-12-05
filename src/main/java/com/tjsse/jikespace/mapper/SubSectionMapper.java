package com.tjsse.jikespace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tjsse.jikespace.entity.SubSection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubSectionMapper extends BaseMapper<SubSection> {
    List<SubSection> findSubSectionBySectionId(Long sectionId);
}
