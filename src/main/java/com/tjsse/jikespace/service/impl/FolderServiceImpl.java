package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.Folder;
import com.tjsse.jikespace.entity.dto.RenameFolderDTO;
import com.tjsse.jikespace.entity.vo.FolderVO;
import com.tjsse.jikespace.mapper.FolderMapper;
import com.tjsse.jikespace.service.FolderService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/8 23:07
 * @since JDK18
 */
@Service
public class FolderServiceImpl implements FolderService {
    @Autowired
    private FolderMapper folderMapper;
    @Override
    public Result createFolder(Long userId, String folderName) {
        LambdaQueryWrapper<Folder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Folder::getUserId,userId);
        queryWrapper.eq(Folder::getFolderName,folderName);
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
        return Result.success(copyList(folders));
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
        Folder folder = folderMapper.selectOne(queryWrapper);
        return folder;
    }
}
