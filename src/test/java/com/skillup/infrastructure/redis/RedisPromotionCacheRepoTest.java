package com.skillup.infrastructure.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisPromotionCacheRepoTest {

    @Autowired
    RedisPromotionCacheRepo redisPromotionCacheRepo;

    public static String KEY = "test_key";
    public static String VALUE = "test_value";

    @Test
    void setAndGetValue() {
        redisPromotionCacheRepo.set(KEY, VALUE);
        String test_result = redisPromotionCacheRepo.get(KEY);
        assertEquals(VALUE, test_result);
    }

}