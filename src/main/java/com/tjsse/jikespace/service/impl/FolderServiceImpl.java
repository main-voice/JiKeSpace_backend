package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.Folder;
import com.tjsse.jikespace.entity.dto.FolderPostDTO;
import com.tjsse.jikespace.entity.dto.RenameFolderDTO;
import com.tjsse.jikespace.entity.vo.CollectPostsVO;
import com.tjsse.jikespace.entity.vo.FolderPostVO;
import com.tjsse.jikespace.entity.vo.FolderVO;
import com.tjsse.jikespace.mapper.FolderMapper;
import com.tjsse.jikespace.service.CollectService;
import com.tjsse.jikespace.service.FolderService;
import com.tjsse.jikespace.service.PostService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/8 23:07
 * @since JDK18
 */

@Service
public class FolderServiceImpl implements FolderService {
    @Autowired
    private FolderMapper folderMapper;
    @Autowired
    private CollectService collectService;
    @Autowired
    private PostService postService;
    @Override
    public Result createFolder(Long userId, String folderName) {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getUserId,userId);
        queryWrapper.eq(Folder::getFolderName,folderName);
        queryWrapper.last("limit 1");
        Folder folder = folderMapper.selectOne(queryWrapper);
        if(folder!=null){
            return Result.fail(-1,"该文件夹名字重复",null);
        }
        else{
            Folder folder1 = new Folder();
            folder1.setFolderName(folderName);
            folder1.setUserId(userId);
            folderMapper.insert(folder1);
            return Result.success(20000,"okk",null);
        }
    }

    @Override
    public Result renameFolder(RenameFolderDTO renameFolderDTO) {
        Long folderId = renameFolderDTO.getFolderId();
        String folderName = renameFolderDTO.getFolderName();

        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getId,folderId);
        queryWrapper.eq(Folder::getFolderName,folderName);
        queryWrapper.last("limit 1");
        Folder folder = folderMapper.selectOne(queryWrapper);
        if(folder==null){
            Folder folderById = this.findFolderById(folderId);
            folderById.setFolderName(folderName);
            folderMapper.updateById(folderById);
            return Result.success(20000,"okk",null);
        }
        else{
            return Result.fail(-1,"已存在该名字的文件夹",null);
        }
    }

    @Override
    public Result getFolders(Long userId) {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getUserId,userId);
        List<Folder> folders = folderMapper.selectList(queryWrapper);
        List<FolderVO> folderVOList = copyList(folders);
        Map<String,Object> map = new HashMap<>();
        map.put("folders",folderVOList);
        return Result.success(map);
    }

    @Override
    public Result getCollectInfo(Long userId, FolderPostDTO folderPostDTO) {
        Long folderId = folderPostDTO.getFolderId();
        Integer curPage = folderPostDTO.getCurPage();
        Integer limit = folderPostDTO.getLimit();
        if(folderId==null||curPage==null||limit==null){
            return Result.fail(-1,"参数有误",null);
        }
        if(folderId==0){
            LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Folder::getUserId,userId);
            queryWrapper.orderByAsc(Folder::getId);
            queryWrapper.last("limit 1");
            Folder folder = folderMapper.selectOne(queryWrapper);
            folderId = folder.getId();
        }

        List<FolderPostVO> folderPostVOList = postService.findPostsByFolderIdWithPage(folderId,curPage,limit);
        CollectPostsVO collectPostsVO = new CollectPostsVO();
        collectPostsVO.setFolderPostDTOList(folderPostVOList);
        if(folderPostVOList!=null){
            collectPostsVO.setTotal(folderPostVOList.size());
        }
        else{
            collectPostsVO.setTotal(0);
        }



        return Result.success(20000,"okk",collectPostsVO);
    }

    @Override
    public Result deleteFolder(Long userId, Long folderId) {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getId,folderId);
        queryWrapper.last("limit 1");
        Folder folder = folderMapper.selectOne(queryWrapper);
        if(folder==null){
            return Result.fail(-1,"参数有误",null);
        }
        folderMapper.deleteById(folder);

        collectService.deleteCollectPostByFolderId(folderId);

        return Result.success(20000,"操作成功",null);
    }

    private List<FolderVO> copyList(List<Folder> folders) {
        List<FolderVO> folderVOList = new ArrayList<>();
        for (Folder folder :
                folders) {
            folderVOList.add(copy(folder));
        }
        return folderVOList;
    }

    private FolderVO copy(Folder folder) {
        FolderVO folderVO = new FolderVO();
        BeanUtils.copyProperties(folder,folderVO);
        return folderVO;
    }

    private Folder findFolderById(Long folderId) {
        LambdaQueryWrapper<Folder> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getId,folderId);
        queryWrapper.last("limit 1");
        Folder folder = folderMapper.selectOne(queryWrapper);
        return folder;
    }
}
