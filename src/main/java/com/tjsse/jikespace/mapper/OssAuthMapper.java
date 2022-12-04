package com.tjsse.jikespace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tjsse.jikespace.entity.OssAuth;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: JiKeSpace
 * @description: mapper for access oss service
 * @packagename: com.tjsse.jikespace.mapper
 * @author: peng peng
 * @date: 2022-12-04 11:31
 **/
@Mapper
public interface OssAuthMapper extends BaseMapper<OssAuth> {
}
