package com.zhliang.pzy.redis.lock.config;

import com.zhliang.pzy.redis.lock.RedissonLock;
import com.zhliang.pzy.redis.lock.RedissonManager;
import com.zhliang.pzy.redis.lock.annotation.LockAspectHandler;
import com.zhliang.pzy.redis.lock.prop.RedissonProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.redisson.Redisson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redisson自动化配置
 */
@Configuration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 2)
    public RedissonLock redissonLock(RedissonManager redissonManager) {
        RedissonLock redissonLock = new RedissonLock(redissonManager);
        LOGGER.info("[RedissonLock]组装完毕");
        return redissonLock;
    }

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 1)
    public RedissonManager redissonManager(RedissonProperties redissonProperties) {
        RedissonManager redissonManager = new RedissonManager(redissonProperties);
        LOGGER.info("[RedissonManager]组装完毕,当前连接方式:" + redissonProperties.getType() + ",连接地址:" + redissonProperties.getAddress());
        return redissonManager;
    }

    @Bean
    public LockAspectHandler distributedLockHandler(){
        return new LockAspectHandler();
    }
}

