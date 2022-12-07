package com.tjsse.jikespace.service;

/**
 * @program: JiKeSpace
 * @description: service for send email
 * @packagename: com.tjsse.jikespace.service
 * @author: peng peng
 * @date: 2022-12-06 23:21
 **/
public interface EmailService {
    void sendEmailVerifyCode(String receiver);
    void storeVerifyCode(String code, String receiver);

    boolean checkVerifyCode(String email, String code);
}
