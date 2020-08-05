package com.zhliang.pzy.redis.lock.strategy.impl;

import com.zhliang.pzy.redis.lock.constant.GlobalConstant;
import com.zhliang.pzy.redis.lock.prop.RedissonProperties;
import com.zhliang.pzy.redis.lock.strategy.RedissonConfigService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单机部署Redisson配置
 */
public class StandaloneConfigImpl implements RedissonConfigService {

    private static final Logger log = LoggerFactory.getLogger(StandaloneConfigImpl.class);

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String redisAddr = GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            //密码可以为空
            if (StringUtils.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
            log.info("初始化[单机部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("单机部署 Redisson init error", e);
        }
        return config;
    }
}
