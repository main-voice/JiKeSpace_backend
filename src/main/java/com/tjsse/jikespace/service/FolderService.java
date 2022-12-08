package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.dto.RenameFolderDTO;
import com.tjsse.jikespace.utils.Result;

public interface FolderService {
    Result createFolder(Long userId, String folderName);

    Result renameFolder(RenameFolderDTO renameFolderDTO);

    Result getFolders(Long userId);
}
