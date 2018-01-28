package com.maimai.common.redis.impl;

import javax.annotation.Resource;

import com.maimai.common.redis.RedisService;

import redis.clients.jedis.JedisCluster;

/**
 * 描述:Jedis集群实现类
 * mlsama
 * 2018/1/21 16:53
 */
public class JedisClusterServiceImpl implements RedisService {
	/** 注入JedisCluster */
	@Resource
	private JedisCluster jedisCluster;
	
	/**
	 * 设置键与值
	 * @param key 键
	 * @param value 值
	 * @return 状态码
	 */
	public String set(String key, String value){
		return jedisCluster.set(key, value);
	}
	/**
	 * 设置键与值
	 * @param key 键
	 * @param value 值
	 * @param seconds 秒数
	 * @return 状态码
	 */
	public String setex(String key, String value, int seconds){
		return jedisCluster.setex(key, seconds, value);
	}
	/**
	 * 根据键获取值 
	 * @param key 键 
	 * @return 值
	 */
	public String get(String key){
		return jedisCluster.get(key);
	}
	/**
	 * 删除指定键
	 * @param key 键
	 * @return 状态码
	 */
	public Long del(String key){
		return jedisCluster.del(key);
	}
	/**
	 * 设置键的生存时间
	 * @param key 键
	 * @param seconds 秒
	 * @return 状态码
	 */
	public Long expire(String key, int seconds){
		return jedisCluster.expire(key, seconds);
	}
	/**
	 * 获取键的自增长的值
	 * @param key 键
	 * @return 自增长的值
	 */
	public Long incr(String key){
		return jedisCluster.incr(key);
	}
}
