package com.tjsse.jikespace.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: JiKeSpace
 * @description: class for access aliyun oss service
 * @packagename: com.tjsse.jikespace.entity
 * @author: peng peng
 * @date: 2022-12-04 11:28
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "oss_auth")
public class OssAuth {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String endpoint;
    private String keyId;
    private String keySecret;
    private String bucketName;
}
