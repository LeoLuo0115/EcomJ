package com.skillup.infrastructure.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisCacheRepoTest {
    @Autowired
    RedisPromotionCacheRepo redisService;


    public static String KEY = "test_key";
    public static String VALUE = "test_value";

//    @Test
//    void setAndGetValue() {
//        redisService.set(KEY, VALUE);
//        String res = redisService.get(KEY);
//        System.out.println(res);
//
//        // assertEquals(res, VALUE);
//    }

}