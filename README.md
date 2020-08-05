# 基于redisson实现的分布式锁
> 支持注解@Lock方式和常规加锁方式。使用策略+工厂模式慢则不同环境的部署

[演示demo](https://github.com/zhangliang1024/spring-fox-spring-boot-sample)

### 一、分布式锁的条件
> 分布式锁要满足的一些条件
```markdown
1. 互斥：在分布式高并发环境下，要保证同一时刻只能有一个线程获得锁。
2. 防止死锁：防止因为系统宕机或故障导致锁无法释放引起的死锁问题，所以锁必须设置过期时间。
3. 高性能：对于访问量大的共享资源，要减少锁的等待时间，避免线程阻塞。
   a. 锁的颗粒度要尽可能的小
   b. 锁的范围要尽量小
4. 重入：同一个线程可以重复拿到同一个锁。
```

### 二、使用方式
- 加锁方式
> 1. 注解方式
```java
@Lock(value="goods", leaseTime=5)
public String lockDecreaseStock() {
    //业务逻辑
}
```
> 2. 常规方式
```java
redissonLock.lock("lock", 10L);
    //业务逻辑
if (redissonLock.isHeldByCurrentThread("lock")) {
    redissonLock.unlock("lock");
}
```

- 部署方式
> 支持 standalone sentinel cluster masterslave 四种方式，通过配置文件指定
```yaml
redisson:
  lock:
    server:
      address: 127.0.0.1:6379
      type: standalone
      database: 0
```

- 推荐使用方式
> 常规加锁。注解方式加锁为整个方法，颗粒度太大。

### 参考文档
[spring-boot-distributed-redisson](https://github.com/yudiandemingzi/spring-boot-distributed-redisson)
[Redisson实现分布式锁](https://www.cnblogs.com/qdhxhz/p/11046905.html)

