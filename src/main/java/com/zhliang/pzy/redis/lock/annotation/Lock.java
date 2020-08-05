package com.zhliang.pzy.redis.lock.annotation;

import java.lang.annotation.*;

/**
 * 基于注解的分布式式锁
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {

    /**
     * 锁的名称
     */
    String value() default "redis-lock";

    /**
     * 锁的有效时间
     */
    int leaseTime() default 10;
}


