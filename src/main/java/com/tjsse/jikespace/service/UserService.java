package com.tjsse.jikespace.service;

import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.entity.dto.EditEmailDTO;
import com.tjsse.jikespace.entity.dto.PasswordDTO;
import com.tjsse.jikespace.entity.dto.UserInfoDTO;
import com.tjsse.jikespace.utils.Result;

/**
 * @program: JiKeSpace
 * @description:
 * @packagename: com.tjsse.jikespace.service
 * @author: peng peng
 * @date: 2022-12-02 15:22
 **/
public interface UserService {
    Result getUserInfo();

    User findUserById(Long userId);

    Long findUserIdByCommentId(Long commentId);

    Result forgetPassword(String verifyCode, String email, String newPassword);

    Result sendEmailVerifyCode(String email);

    Result editEmail(Long userId, EditEmailDTO editEmailDTO);

    Result getUserInformation(Long userId);

    Result editUserInfo(Long userId, UserInfoDTO userInfoDTO);

    Result editPassword(Long userId, PasswordDTO passwordDTO);
}
