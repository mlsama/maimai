package com.maimai.common.redis;
import redis.clients.jedis.Jedis;
/**
 * mlsama
 * 2018/1/21 16:47
 * 描述:Jedis执行时，需要回调的接口
 */
public interface RedisFunction<T> {
    T callback(Jedis jedis);
}
