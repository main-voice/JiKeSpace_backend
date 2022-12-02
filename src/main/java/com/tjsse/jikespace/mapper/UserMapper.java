package com.tjsse.jikespace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tjsse.jikespace.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: JiKeSpace
 * @description: mapper for user entity
 * @packagename: com.tjsse.jikespace.mapper
 * @author: peng peng
 * @date: 2022-11-29 18:08
 **/

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
