package com.zhliang.pzy.redis.lock.annotation;

import com.zhliang.pzy.redis.lock.RedissonLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Redisson分布式锁注解解析器
 */
@Aspect
public class LockAspectHandler {

    private static final Logger log = LoggerFactory.getLogger(LockAspectHandler.class);

    @Autowired
    RedissonLock redissonLock;

    @Around("@annotation(lock)")
    public Object around(ProceedingJoinPoint joinPoint, Lock lock) {
        log.info("[开始]执行RedisLock环绕通知,获取Redis分布式锁开始");
        //获取锁名称
        String lockName = lock.value();
        //获取超时时间，默认10秒
        int leaseTime = lock.leaseTime();
        redissonLock.lock(lockName, leaseTime);
        try {
            log.info("获取Redis分布式锁[成功]，加锁完成，开始执行业务逻辑！");
            return joinPoint.proceed();
        } catch (Throwable th) {
            log.error("获取Redis分布式锁[异常]，加锁失败", th);
            th.printStackTrace();
            throw new RuntimeException(th.getMessage());
        } finally {
            //如果该线程还持有该锁，那么释放该锁。如果该线程不持有该锁，说明该线程的锁已到过期时间，自动释放锁
            if (redissonLock.isHeldByCurrentThread(lockName)) {
                redissonLock.unlock(lockName);
            }
            log.info("释放Redis分布式锁[成功]，解锁完成，结束业务逻辑！");
        }
    }
}
