package com.tjsse.jikespace.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tjsse.jikespace.entity.OssAuth;
import com.tjsse.jikespace.mapper.OssAuthMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @program: JiKeSpace
 * @description: service for images
 * @packagename: com.tjsse.jikespace.utils
 * @author: peng peng
 * @date: 2022-12-04 11:32
 **/
@Service
public class OssService {

    private final OssAuth ossAuth;

    public OssService(OssAuthMapper ossAuthMapper) {
        QueryWrapper<OssAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", 1);

        this.ossAuth = ossAuthMapper.selectOne(queryWrapper);
    }

    public String uploadFile(MultipartFile file)
    {
        String endpoint = ossAuth.getEndpoint();
        String accessKeyId = ossAuth.getKeyId();
        String accessKeySecret = ossAuth.getKeySecret();
        String bucketName = ossAuth.getBucketName();

        InputStream inputStream = null;

        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // get input stream of file
            inputStream = file.getInputStream();

            // get name of file
            String fileName = file.getOriginalFilename();

            // make the name of file unique
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + "-" + fileName;

            // sort by date
            String datePath = String.valueOf(LocalDate.now());

            fileName = datePath + "/" + fileName;

            System.out.println("fileName : " + fileName);

            ossClient.putObject(bucketName, fileName, inputStream);

            // shut down the connection
            ossClient.shutdown();

            return "https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    // beginPath 是http后面紧跟的路径名
    public String uploadFile(MultipartFile file, String beginPath)
    {
        String endpoint = ossAuth.getEndpoint();
        String accessKeyId = ossAuth.getKeyId();
        String accessKeySecret = ossAuth.getKeySecret();
        String bucketName = ossAuth.getBucketName();

        InputStream inputStream = null;

        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // get input stream of file
            inputStream = file.getInputStream();

            // get name of file
            String fileName = file.getOriginalFilename();

            // make the name of file unique
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = beginPath + "/" + uuid + "-" + fileName;

            System.out.println("fileName : " + fileName);

            ossClient.putObject(bucketName, fileName, inputStream);

            // shut down the connection
            ossClient.shutdown();

            return "https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
