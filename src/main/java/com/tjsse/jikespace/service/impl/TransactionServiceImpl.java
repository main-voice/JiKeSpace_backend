package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjsse.jikespace.entity.*;
import com.tjsse.jikespace.entity.dto.NewTransactionDTO;
import com.tjsse.jikespace.entity.dto.SearchTransactionDTO;
import com.tjsse.jikespace.entity.vo.SubtagVO;
import com.tjsse.jikespace.entity.vo.TagVO;
import com.tjsse.jikespace.entity.vo.TransactionPageVO;
import com.tjsse.jikespace.entity.vo.TransactionVO;
import com.tjsse.jikespace.mapper.*;
import com.tjsse.jikespace.service.TransactionService;
import com.tjsse.jikespace.utils.OssService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.tjsse.jikespace.utils.JKCode.*;

/**
 * @program: JiKeSpace
 * @description:
 * @packagename: com.tjsse.jikespace.service.impl
 * @author: peng peng
 * @date: 2022-12-10 14:33
 **/
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private SubtagMapper subtagMapper;

    @Autowired
    private PostAndBodyMapper postAndBodyMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionImagesMapper transactionImagesMapper;

    @Autowired
    OssService ossService;

    @Autowired
    UserMapper userMapper;

    @Override
    public Result getAllTags() {
        List<TagVO> tagVOList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();

        tagList = tagMapper.selectList(null);

        for (Tag tag : tagList)
        {
            TagVO tagVO = new TagVO();
            tagVO.setTagId(tag.getId());
            tagVO.setTagName(tag.getTagName());

            List<Subtag> subtagList = new ArrayList<>();
            QueryWrapper<Subtag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tag_id", tag.getId());
            subtagList = subtagMapper.selectList(queryWrapper);

            List<SubtagVO> subtagVOList = new ArrayList<>();
            for (Subtag subtag : subtagList) {
                SubtagVO subtagVO = new SubtagVO(subtag.getId(), subtag.getSubtagName());
                subtagVOList.add(subtagVO);
            }
            tagVO.setSubtagList(subtagVOList);

            tagVOList.add(tagVO);
        }

        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), tagVOList);
    }

    @Override
    public Result getTransactionInfoByPage(SearchTransactionDTO searchTransactionDTO) {
        QueryWrapper<TransactionPost> queryWrapper = new QueryWrapper<>();
        String type = searchTransactionDTO.getType();
        Integer intType = 0;
        if (Objects.equals(type, "seek")) {
            intType = BUY_POST.getCode();
        } else if (Objects.equals(type, "sell")){
            intType = SELL_POST.getCode();
        }

        if (searchTransactionDTO.getCampus() != null) {
            queryWrapper.eq("campus", searchTransactionDTO.getCampus());
        }
        if (searchTransactionDTO.getSearchContent() != null) {
            queryWrapper.like("title", searchTransactionDTO.getSearchContent())
                    .or()
                    .like("summary", searchTransactionDTO.getSearchContent());
        }
        if (searchTransactionDTO.getType() != null) {
            queryWrapper.eq("post_type", intType);
        }
        if (searchTransactionDTO.getTagId() != null) {
            queryWrapper.eq("tag_id", searchTransactionDTO.getTagId());
        }
        if (searchTransactionDTO.getSubtagId() != null) {
            queryWrapper.eq("subtag_id", searchTransactionDTO.getSubtagId());
        }
        queryWrapper.eq("is_deleted", false);

        // 分页查询
        Page<TransactionPost> emptyPage = new Page<>(searchTransactionDTO.getCurPage(), searchTransactionDTO.getLimit());
        Page<TransactionPost> transactionPostPage = transactionMapper.selectPage(emptyPage, queryWrapper);

        if (transactionPostPage.getRecords().size() == 0) {
            return Result.fail(OTHER_ERROR.getCode(), "交易贴为空或者查询出错", null);
        }

        // 查询后转为VO返回给前端
        List<TransactionPageVO> transactionPageVOList = new ArrayList<>();
        for (TransactionPost transactionPost : transactionPostPage.getRecords())
        {
            TransactionPageVO transactionPageVO = new TransactionPageVO();
            BeanUtils.copyProperties(transactionPost, transactionPageVO);
            transactionPageVOList.add(transactionPageVO);
        }
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), transactionPageVOList);
    }

    @Override
    public Result getTransactionInfoById(Long id) {
        TransactionPost transactionPost = transactionMapper.selectById(id);
        if (transactionPost == null || transactionPost.isDeleted()) {
            return Result.fail(OTHER_ERROR.getCode(), "该id不存在或已删除", null);
        }

        TransactionVO transactionVO = new TransactionVO();
        BeanUtils.copyProperties(transactionPost, transactionVO);

        // avatar and name
        User user = userMapper.selectById(transactionPost.getAuthorId());
        if (user == null || user.getIsDeleted()) {
            return Result.fail(OTHER_ERROR.getCode(), "用户不存在", null);
        }
        transactionVO.setAvatar(user.getAvatar());
        transactionVO.setAuthorName(user.getUsername());
        // content
        QueryWrapper<PostAndBody> postAndBodyQueryWrapper = new QueryWrapper<>();
        postAndBodyQueryWrapper.eq("post_id", transactionPost.getId());
        String content = postAndBodyMapper.selectOne(postAndBodyQueryWrapper).getContent();
        transactionVO.setContent(content);
        // images
        QueryWrapper<TransactionImages> transactionImagesQueryWrapper = new QueryWrapper<>();
        transactionImagesQueryWrapper.eq("transaction_id", transactionPost.getId());
        List<TransactionImages> imagesList = transactionImagesMapper.selectList(transactionImagesQueryWrapper);
        List<String> imagesUrl = new ArrayList<>();
        for (TransactionImages trImage : imagesList) {
            imagesUrl.add(trImage.getImageUrl());
        }
        transactionVO.setImagesList(imagesUrl);
        // viewCounts ++
        UpdateWrapper<TransactionPost> updateWrapperTranPostViewCount = new UpdateWrapper<>();
        Integer viewCount = transactionVO.getViewCounts();
        if (viewCount == null) {
            viewCount = 0;
        }
        viewCount = viewCount + 1;
        updateWrapperTranPostViewCount.eq("id", transactionPost.getId())
                .set("view_counts", viewCount);
        transactionMapper.update(null, updateWrapperTranPostViewCount);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), transactionVO);
    }

    @Override
    public Result createTransactionPost(Long authorId, NewTransactionDTO newTransactionDTO) {
        TransactionPost transactionPost = new TransactionPost();
        // copy common properties
        BeanUtils.copyProperties(newTransactionDTO, transactionPost);
        // set create time
        transactionPost.setPublishTime(LocalDateTime.now());
        transactionPost.setAuthorId(authorId);
        transactionPost.setViewCounts(0);

        if (Objects.equals(newTransactionDTO.getType(), "seek")) {
            transactionPost.setPostType(BUY_POST.getCode());
        } else if (Objects.equals(newTransactionDTO.getType(), "sell")) {
            transactionPost.setPostType(SELL_POST.getCode());
        } else {
            return Result.fail(OTHER_ERROR.getCode(), "交易贴类型不可为空", null);
        }

        // 检测tagId与subtagId是否存在
        Tag tag = tagMapper.selectById(newTransactionDTO.getTagId());
        if (tag == null) {
            return Result.fail(OTHER_ERROR.getCode(), "tag不存在", null);
        }
        Subtag subtag = subtagMapper.selectById(newTransactionDTO.getSubtagId());
        if (subtag == null) {
            return Result.fail(OTHER_ERROR.getCode(), "subtag不存在", null);
        }

        // 将所有图片插入到图床，返回链接
        MultipartFile[] imagesFile = newTransactionDTO.getMultipartFiles();
        List<String> imagesUrl = new ArrayList<>();
        for (MultipartFile image : imagesFile) {
            String imageUrl = ossService.uploadFile(image, "transaction");
            imagesUrl.add(imageUrl);
        }

        // 生成post之后，插入数据库
        int insert = transactionMapper.insert(transactionPost);
        if (insert == 0) {
            return Result.fail(OTHER_ERROR.getCode(), "插入交易贴失败", null);
        }

        // 有了postId，再去设置images的数据库
        for (String imageUrl : imagesUrl) {
//            System.out.println(imageUrl);
            TransactionImages transactionImages = TransactionImages.builder()
                    .transactionId(transactionPost.getId()) // 插入后该对象的信息自动更新，所以直接用id信息
                    .imageUrl(imageUrl)
                    .build();
            int insertImage = transactionImagesMapper.insert(transactionImages);
            if (insertImage == 0) {
                return Result.fail(OTHER_ERROR.getCode(), "插入图片失败", null);
            }
        }

        // 有了postId，根据postId去设置对应的body，再回来一个bodyId
        PostAndBody postAndBody = PostAndBody.builder()
                .postId(transactionPost.getId())
                .content(newTransactionDTO.getContent())
                .build();
        int insertBody = postAndBodyMapper.insert(postAndBody);
        if (insertBody == 0) {
            return Result.fail(OTHER_ERROR.getCode(), "插入交易帖内容失败", null);
        }

        // 再把bodyBody的id设置给post的bodyId
        // 同时更新封面图
        UpdateWrapper<TransactionPost> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", transactionPost.getId())
                        .set("body_id", postAndBody.getId());
        if (imagesUrl.size() >= 1) {
            updateWrapper.set("cover_image", imagesUrl.get(0));
        }

        transactionMapper.update(null, updateWrapper);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), transactionPost.getId());
    }

    @Override
    public Result deleteTransactionPost(Long id) {
        TransactionPost postToDelete = transactionMapper.selectById(id);
        if (postToDelete == null) {
            return Result.fail(OTHER_ERROR.getCode(), "待删除的帖子不存在");
        }

        UpdateWrapper<TransactionPost> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id)
                .set("is_deleted", true);
        transactionMapper.update(null, updateWrapper);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), null);
    }
}