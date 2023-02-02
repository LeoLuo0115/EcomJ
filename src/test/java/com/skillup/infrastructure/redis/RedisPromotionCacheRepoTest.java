package com.skillup.infrastructure.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisPromotionCacheRepoTest {

    @Autowired
    RedisPromotionCacheRepo redisService;

    public static String KEY = "test_key";
    public static String VALUE = "test_value";


    @Test
    void setAndGetValue() {
        redisService.set("test_key", "test_value");
        String value = redisService.get("test_key");
        System.out.println(value);
        //assertEquals(value, VALUE);
//        assertThat(value).isEqualTo("test_value");
    }
}