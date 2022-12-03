package com.tjsse.jikespace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tjsse.jikespace.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: JiKeSpace_backend
 * @description: mapper for admin
 * @package_name: com.tjsse.jikespace.mapper
 * @author: peng peng
 * @date: 2022/12/3
 **/
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}