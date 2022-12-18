package com.tjsse.jikespace.service;


import com.tjsse.jikespace.utils.Result;

public interface StudentService {
    Result applyToStudent(Long userId, String s, Integer studentId);
}
