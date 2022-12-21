package com.tjsse.jikespace.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tjsse.jikespace.entity.TransactionPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: JiKeSpace
 * @description: mapper for transaction
 * @packagename: com.tjsse.jikespace.mapper
 * @author: peng peng
 * @date: 2022-12-10 14:30
 **/
@Mapper
public interface TransactionMapper extends BaseMapper<TransactionPost> {
}
