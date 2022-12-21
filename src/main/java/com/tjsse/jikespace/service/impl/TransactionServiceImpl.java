package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tjsse.jikespace.entity.*;
import com.tjsse.jikespace.entity.dto.NewTransactionDTO;
import com.tjsse.jikespace.entity.dto.SearchTransactionDTO;
import com.tjsse.jikespace.entity.vo.*;
import com.tjsse.jikespace.mapper.*;
import com.tjsse.jikespace.service.ThreadService;
import com.tjsse.jikespace.service.TransactionService;
import com.tjsse.jikespace.utils.JKCode;
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
    private TransactionAndBodyMapper transactionAndBodyMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionImagesMapper transactionImagesMapper;

    @Autowired
    private CollectAndTransactionMapper collectAndTransactionMapper;

    @Autowired
    private OssService ossService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    ThreadService threadService;

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
//        System.out.println(intType);

        System.out.println(searchTransactionDTO.getCampusZone());
        queryWrapper.eq(searchTransactionDTO.getType() != null, "post_type", intType)
                .like(searchTransactionDTO.getCampusZone() != null, "campus", searchTransactionDTO.getCampusZone())
                .like(searchTransactionDTO.getSearchContent() != null, "title", searchTransactionDTO.getSearchContent())
                .eq(searchTransactionDTO.getTagId() != null, "tag_id", searchTransactionDTO.getTagId())
                .eq(searchTransactionDTO.getSubtagId() != null, "subtag_id", searchTransactionDTO.getSubtagId())
                .eq("is_deleted", false);

        TransactionPagesVO transactionPagesVO = new TransactionPagesVO();
        List<TransactionPost> list = transactionMapper.selectList(queryWrapper);
        transactionPagesVO.setTotal(list.size());

        // 分页查询
        Page<TransactionPost> emptyPage = new Page<>(searchTransactionDTO.getOffset(), searchTransactionDTO.getLimit());
        Page<TransactionPost> transactionPostPage = transactionMapper.selectPage(emptyPage, queryWrapper);


        // 查询后转为VO返回给前端
        List<TransactionPageVO> transactionPageVOList = new ArrayList<>();
        for (TransactionPost transactionPost : transactionPostPage.getRecords())
        {
            TransactionPageVO transactionPageVO = new TransactionPageVO();
            BeanUtils.copyProperties(transactionPost, transactionPageVO);
            transactionPageVOList.add(transactionPageVO);
        }

        transactionPagesVO.setTransactionPageVOList(transactionPageVOList);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), transactionPagesVO);
    }

    @Override
    public Result getTransactionInfoById(Long id) {
        QueryWrapper<TransactionPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        TransactionPost transactionPost = transactionMapper.selectOne(queryWrapper);
        if (transactionPost == null || transactionPost.isDeleted()) {
            return Result.fail(OTHER_ERROR.getCode(), "该id不存在或已删除", null);
        }

        TransactionVO transactionVO = new TransactionVO();
        BeanUtils.copyProperties(transactionPost, transactionVO);
        // TODO : add tagName and subtagName
        Integer tagId = transactionPost.getTagId();
        Tag tag = tagMapper.selectById(tagId);
        transactionVO.setTagName(tag.getTagName());

        Integer subtagId = transactionPost.getSubtagId();
        Subtag subtag = subtagMapper.selectById(subtagId);
        transactionVO.setSubtagName(subtag.getSubtagName());
        // type
        if (Objects.equals(transactionPost.getPostType(), SELL_POST.getCode())) {
            transactionVO.setType("出售");
        }
        else if (transactionPost.getPostType() == BUY_POST.getCode()) {
            transactionVO.setType("求购");
        }
        // avatar and name
        User user = userMapper.selectById(transactionPost.getAuthorId());
        if (user == null || user.getIsDeleted()) {
            return Result.fail(OTHER_ERROR.getCode(), "用户不存在", null);
        }
        transactionVO.setAvatar(user.getAvatar());
        transactionVO.setAuthorName(user.getUsername());
        // content
        QueryWrapper<TransactionAndBody> postAndBodyQueryWrapper = new QueryWrapper<>();
        postAndBodyQueryWrapper.eq("transaction_id", transactionPost.getId());
        // multi records are queried.
        TransactionAndBody transactionAndBody = transactionAndBodyMapper.selectOne(postAndBodyQueryWrapper);
        if (transactionAndBody == null) {
            return Result.fail(OTHER_ERROR.getCode(), "获取交易贴内容时失败，为空", null);
        }
        String content = transactionAndBody.getContent();
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
        threadService.updateViewCountOfTransactionPost(transactionMapper, transactionPost);
//        UpdateWrapper<TransactionPost> updateWrapperTranPostViewCount = new UpdateWrapper<>();
//        Integer viewCount = transactionVO.getViewCounts();
//        if (viewCount == null) {
//            viewCount = 0;
//        }
//        viewCount = viewCount + 1;
//        updateWrapperTranPostViewCount.eq("id", transactionPost.getId())
//                .set("view_counts", viewCount);
//        transactionMapper.update(null, updateWrapperTranPostViewCount);
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
        transactionPost.setSummary(newTransactionDTO.getContent());

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
        TransactionAndBody transactionAndBody = TransactionAndBody.builder()
                .transactionId(transactionPost.getId())
                .content(newTransactionDTO.getContent())
                .build();

        int insertBody = transactionAndBodyMapper.insert(transactionAndBody);
        if (insertBody == 0) {
            return Result.fail(OTHER_ERROR.getCode(), "插入交易帖内容失败", null);
        }

        // 再把bodyBody的id设置给post的bodyId
        // 同时更新封面图
        UpdateWrapper<TransactionPost> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", transactionPost.getId())
                        .set("body_id", transactionAndBody.getId());
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

    @Override
    public Result collectTransactionPost(Long userId, Long postId) {
        QueryWrapper<CollectAndTransaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("transaction_post_id", postId);

        CollectAndTransaction collectAndTransaction = collectAndTransactionMapper.selectOne(queryWrapper);
        // if not exists, insert one
        if (collectAndTransaction == null) {
            CollectAndTransaction newCollectTransaction = new CollectAndTransaction();
            newCollectTransaction.setTransactionPostId(postId);
            newCollectTransaction.setUserId(userId);
            collectAndTransactionMapper.insert(newCollectTransaction);
            return Result.success(SUCCESS.getCode());
        }
        // if already exists, remove it.
        collectAndTransactionMapper.delete(queryWrapper);
        return Result.success(SUCCESS.getCode());
    }

    @Override
    public Result getUserSellTrans(Long userId, Integer offset, Integer limit) {
        QueryWrapper<TransactionPost> queryWrapper = new QueryWrapper<>();
        Integer intType = SELL_POST.getCode();
        TransactionPagesVO transactionPagesVO = new TransactionPagesVO();

        queryWrapper.eq("post_type", intType)
                .eq("is_deleted", false)
                .eq("author_id", userId);

        List<TransactionPost> list = transactionMapper.selectList(queryWrapper);
        transactionPagesVO.setTotal(list.size());

        // 分页查询
        Page<TransactionPost> emptyPage = new Page<>(offset, limit);
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

        transactionPagesVO.setTransactionPageVOList(transactionPageVOList);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), transactionPagesVO);
    }

    @Override
    public Result getUserSeekTrans(Long userId, Integer offset, Integer limit) {
        QueryWrapper<TransactionPost> queryWrapper = new QueryWrapper<>();
        Integer intType = BUY_POST.getCode();
        TransactionPagesVO transactionPagesVO = new TransactionPagesVO();


        queryWrapper.eq("post_type", intType)
                .eq("is_deleted", false)
                .eq("author_id", userId);

        List<TransactionPost> list = transactionMapper.selectList(queryWrapper);
        transactionPagesVO.setTotal(list.size());

        // 分页查询
        Page<TransactionPost> emptyPage = new Page<>(offset, limit);
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

        transactionPagesVO.setTransactionPageVOList(transactionPageVOList);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), transactionPagesVO);
    }

    @Override
    public Result getMyCollectTransaction(Long userId, Integer offset, Integer limit) {
        QueryWrapper<CollectAndTransaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<CollectAndTransaction> originList = collectAndTransactionMapper.selectList(queryWrapper);
        // new obj
        TransactionPagesVO transactionPagesVO = new TransactionPagesVO();
        transactionPagesVO.setTotal(originList.size());

        List<TransactionPageVO> transactionPageVOList = new ArrayList<>();
        for (CollectAndTransaction entity
                : originList) {
            Long tranId = entity.getTransactionPostId();
            TransactionPost post = transactionMapper.selectById(tranId);
            TransactionPageVO transactionPageVO = new TransactionPageVO();
            BeanUtils.copyProperties(post, transactionPageVO);
            transactionPageVOList.add(transactionPageVO);
        }
        transactionPagesVO.setTransactionPageVOList(transactionPageVOList);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), transactionPagesVO);
    }
}
