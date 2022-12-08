package com.tjsse.jikespace.service.impl;

import com.tjsse.jikespace.service.EmailService;
import com.tjsse.jikespace.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.tjsse.jikespace.utils.JKCode.EMAIL_CODE_PREFIX;

/**
 * @program: JiKeSpace
 * @description: impl for email service
 * @packagename: com.tjsse.jikespace.service.impl
 * @author: peng peng
 * @date: 2022-12-06 23:22
 **/
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSenderImpl mailSender;

    // get sender from application.properties
    @Value("jk_space@163.com")
    private String sender;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final String CODE_NUMBER = "0123456789";
    private static final Random RANDOM = new Random();

    private String generateVerifyCode() {
        char[] verifyCodeArr = new char[6];
        for (int i = 0; i < verifyCodeArr.length; i ++ ) {
            verifyCodeArr[i] = CODE_NUMBER.charAt(RANDOM.nextInt(CODE_NUMBER.length()));
        }
        return new String(verifyCodeArr);
    }

    //  send email
    @Override
    public void sendEmailVerifyCode(String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        String verifyCode = generateVerifyCode();

        message.setSubject("JK-Space-济客空间论坛验证码");	//设置邮件标题
        message.setText("尊敬的用户，您好：\n"
                + "\n本次请求的邮件验证码为：" + verifyCode + "，本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");	//设置邮件正文
        message.setTo(receiver);
        message.setFrom(sender);
        mailSender.send(message);

        storeVerifyCode(verifyCode, receiver);
    }

    // 将验证码存储到redis缓存中
    public void storeVerifyCode(String code, String receiver) {
        // store the verification code
        String redisKey = EMAIL_CODE_PREFIX.getMsg() + receiver;
        RedisUtils redisUtils = new RedisUtils(stringRedisTemplate);
        // if the key exists, delete it
        if (redisUtils.exists(redisKey)) {
            redisUtils.del(redisKey);
        }
        // reset，生存周期为5分钟
        redisUtils.set(redisKey, code, 5);
    }

    @Override
    public boolean checkVerifyCode(String email, String code) {
        String finalCode = EMAIL_CODE_PREFIX.getMsg() + email;
        RedisUtils redisUtils = new RedisUtils(stringRedisTemplate);
        if (redisUtils.exists(finalCode)) {
            redisUtils.del(finalCode);
            return true;
        }
        return false;
    }

}
