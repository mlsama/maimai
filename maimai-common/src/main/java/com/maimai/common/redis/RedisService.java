package com.maimai.common.redis;
/**
 * mlsama
 * 2018/1/21 16:48
 * 描述:Jedis操作接口
 */
public interface RedisService {
	/**
	 * 设置键与值
	 * @param key 键
	 * @param value 值
	 * @return 状态码
	 */
	String set(String key, String value);
	/**
	 * 设置键与值
	 * @param key 键
	 * @param value 值
	 * @param seconds 秒数
	 * @return 状态码
	 */
	String setex(String key, String value, int seconds);
	/**
	 * 根据键获取值 
	 * @param key 键 
	 * @return 值
	 */
	String get(String key);
	/**
	 * 删除指定键
	 * @param key 键
	 * @return 状态码
	 */
	Long del(String key);
	/**
	 * 设置键的生存时间
	 * @param key 键
	 * @param seconds 秒
	 * @return 状态码
	 */
	Long expire(String key, int seconds);
	/**
	 * 获取键的自增长的值
	 * @param key 键
	 * @return 自增长的值
	 */
	Long incr(String key);
}