package com.maimai.common.redis.impl;

import javax.annotation.Resource;

import com.maimai.common.redis.RedisFunction;
import com.maimai.common.redis.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 描述:Jedis单机版实现类
 * mlsama
 * 2018/1/21 17:10
 */
public class JedisPoolServiceImpl implements RedisService {
	
	/** 注入JedisPool连接池 */
	@Resource
	private JedisPool jedisPool;
	
	
	/** 执行Jedis通用的方法 */
	private <T> T execute(RedisFunction<T> redisFunction){
		/** 获取Jedis */
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			return redisFunction.callback(jedis);
		}finally{
			if (jedis != null) jedis.close();
		}
	}
	/**
	 * 设置键与值
	 * @param key 键
	 * @param value 值
	 * @return 状态码 
	 */ 
	public String set(final String key, final String value){
		return execute(new RedisFunction<String>() {
			@Override
			public String callback(Jedis jedis) {
				return jedis.set(key, value);
			}
		});
	}
	/**
	 * 设置键与值
	 * @param key 键
	 * @param value 值
	 * @param seconds 秒数
	 * @return 状态码
	 */
	public String setex(final String key, final String value, final int seconds){
		return execute(new RedisFunction<String>() {
			@Override
			public String callback(Jedis jedis) {
				return jedis.setex(key, seconds, value);
			}
		});
	}
	/**
	 * 根据键获取值 
	 * @param key 键 
	 * @return 值
	 */
	public String get(final String key){
		return execute(new RedisFunction<String>() {
			@Override
			public String callback(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}
	/**
	 * 删除指定键
	 * @param key 键
	 * @return 状态码
	 */
	public Long del(final String key){
		return execute(new RedisFunction<Long>() {
			@Override
			public Long callback(Jedis jedis) {
				return jedis.del(key);
			}
		});
	}
	/**
	 * 设置键的生存时间
	 * @param key 键
	 * @param seconds 秒
	 * @return 状态码
	 */
	public Long expire(final String key,final int seconds){
		return execute(new RedisFunction<Long>() {
			@Override
			public Long callback(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
	}
	/**
	 * 获取键的自增长的值
	 * @param key 键
	 * @return 自增长的值
	 */
	public Long incr(final String key){
		return execute(new RedisFunction<Long>() {
			@Override
			public Long callback(Jedis jedis) {
				return jedis.incr(key);
			}
		});
	}
}