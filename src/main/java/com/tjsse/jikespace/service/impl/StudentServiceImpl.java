package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.Student;
import com.tjsse.jikespace.mapper.StudentMapper;
import com.tjsse.jikespace.service.StudentService;
import com.tjsse.jikespace.service.UserService;
import com.tjsse.jikespace.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wlf 1557177832@qq.com
 * @version 2022/12/18 14:52
 * @since JDK18
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private UserService userService;
    @Override
    public Result applyToStudent(Long userId, String s, Integer studentId) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getStudentId,studentId);
        queryWrapper.last("limit 1");
        Student student = studentMapper.selectOne(queryWrapper);
        if(student!=null){
            return Result.fail(-1,"该学生身份已被其他账号注册",null);
        }
        else{
            Student student1 = new Student();
            student1.setStudentId(studentId);
            student1.setStudentPic(s);
            student1.setIsPassed(false);
            student1.setUserId(userId);
            student1.setUsername(userService.findUserById(userId).getUsername());
            studentMapper.insert(student1);
            return Result.success(20000,"okk",null);
        }
    }
}
