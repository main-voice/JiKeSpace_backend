package com.tjsse.jikespace.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @program: JiKeSpace
 * @description: utils for redis which store email verification code or sms code
 * @packagename: com.tjsse.jikespace.utils
 * @author: peng peng
 * @date: 2022-12-07 13:52
 **/
public class RedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUtils(StringRedisTemplate redis) {
        stringRedisTemplate = redis;
    }

    public boolean setStrValue(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // get object based on key
    public Object get(String key){
        return key == null ? null : stringRedisTemplate.opsForValue().get(key);
    }

    public boolean set(String key,String value, long time){
        try {
            if(time > 0){
                stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
            }else{
                stringRedisTemplate.opsForValue().set(key, value);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean exists(String key){
        try {
            return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void del(String key){
        try {
            if(key != null && key.length() > 0){
                stringRedisTemplate.delete(key);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
