package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tjsse.jikespace.auth_user.AppUser;
import com.tjsse.jikespace.entity.Comment;
import com.tjsse.jikespace.entity.User;
import com.tjsse.jikespace.entity.dto.EditEmailDTO;
import com.tjsse.jikespace.entity.dto.PasswordDTO;
import com.tjsse.jikespace.entity.dto.UserInfoDTO;
import com.tjsse.jikespace.entity.vo.UserVO;
import com.tjsse.jikespace.mapper.CommentMapper;
import com.tjsse.jikespace.mapper.UserMapper;
import com.tjsse.jikespace.service.EmailService;
import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tjsse.jikespace.utils.JKCode.*;

/**
 * @program: JiKeSpace
 * @description:
 * @packagename: com.tjsse.jikespace.service.impl
 * @author: peng peng
 * @date: 2022-12-02 15:24
 **/

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Result getUserInfo() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        // get context
// login email token email database id
        AppUser loginUser = (AppUser) usernamePasswordAuthenticationToken.getPrincipal();
        User user = loginUser.getUser();

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), userVO);
    }

    @Override
    public User findUserById(Long userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,userId);
        queryWrapper.last("limit 1");
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Long findUserIdByCommentId(Long commentId) {
        LambdaQueryWrapper<Comment> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getId,commentId);
        queryWrapper.last("limit 1");
        Comment comment = commentMapper.selectOne(queryWrapper);
        return comment.getAuthorId();
    }

    @Override
    public Result forgetPassword(String verifyCode, String email, String newPassword) {
        // 检测验证码与邮箱是否正确
        boolean checkResult = emailService.checkVerifyCode(email, verifyCode);
        if (!checkResult) {
            return Result.fail(OTHER_ERROR.getCode(), "验证码错误", null);
        }
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("email", email);
        userUpdateWrapper.set("password", passwordEncoder.encode(newPassword));
        userMapper.update(null, userUpdateWrapper);
        return Result.success(SUCCESS.getCode(), SUCCESS.getMsg(), null);
    }

    @Override
    public Result sendEmailVerifyCode(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email)
                .eq("is_deleted", false);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return Result.fail(ACCOUNT_NOT_EXIST.getCode(), "该邮箱未使用，请先注册", null);
        }
        try {
            emailService.sendEmailVerifyCode(email);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(OTHER_ERROR.getCode(), "发送邮件失败", null);
        }
        return Result.success(SUCCESS.getCode(), "发送验证码成功", null);
    }

    @Override
    public Result editEmail(Long userId, EditEmailDTO editEmailDTO) {
        String email = editEmailDTO.getEmail();
        String password = editEmailDTO.getPassword();
        if(email==null||password==null){
            return Result.fail(-1,"参数有误",null);
        }

        User user = this.findUserById(userId);
        String password1 = user.getPassword();
        boolean matches = passwordEncoder.matches(password, password1);
        if(matches){
            if(user.getEmail()==email){
                return Result.fail(-1,"新邮箱与原邮箱重复",null);
            }
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getEmail,email);
            queryWrapper.eq(User::getIsDeleted,false);
            List<User> users = userMapper.selectList(queryWrapper);
            if(users.size()!=0){
                return Result.fail(-1,"该邮箱已与其他账号绑定",null);
            }
            user.setEmail(email);
            userMapper.updateById(user);
            return Result.success(true);
        }
        else{
            return Result.success(false);
        }
    }

    @Override
    public Result getUserInformation(Long userId) {
        User user = this.findUserById(userId);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return Result.success(20000,"okk",userVO);
    }

    @Override
    public Result editUserInfo(Long userId, UserInfoDTO userInfoDTO) {
        String nickname = userInfoDTO.getNickname();
        String phone = userInfoDTO.getPhone();
        String intro = userInfoDTO.getIntro();
        String gender = userInfoDTO.getGender();
        if(nickname==null||phone==null||intro==null||gender==null){
            return Result.fail(-1,"参数有误",null);
        }

        User user = this.findUserById(userId);

        user.setNickname(nickname);
        user.setPhoneNumber(phone);
        user.setGender(gender);
        user.setSummary(intro);

        userMapper.updateById(user);

        return Result.success(20000,"okk",null);
    }

    @Override
    public Result editPassword(Long userId, PasswordDTO passwordDTO) {
        String newPassword = passwordDTO.getNewPassword();
        String oldPassword = passwordDTO.getOldPassword();
        if(newPassword==null||oldPassword==null){
            return Result.fail(-1,"参数错误",null);
        }
        User user = this.findUserById(userId);
        String password = user.getPassword();
        boolean matches = passwordEncoder.matches(oldPassword, password);
        if(matches){
            if ( oldPassword==newPassword){
                return Result.fail(-1,"新密码与旧密码重复",false);
            }
            user.setPassword(newPassword);
            userMapper.updateById(user);
            return Result.success(20000,"okk",true);
        }
        else{
            return Result.fail(-1,"密码错误或新密码与旧密码重复",false);
        }
    }

}
