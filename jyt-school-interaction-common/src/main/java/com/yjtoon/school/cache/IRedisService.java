package com.yjtoon.school.cache;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Description： redis 常用操作
 * @author: SHENZL
 * @since: 2017/11/9 11:27
 */
public interface IRedisService {

    /***
     * 检查redis 中是否存在key
     * @param key
     * @return
     */
    public Boolean hasKey(String key);

    /***
     * 从缓存中得到字符串
     * @param key
     * @return
     */
    public String getString(String key);
    public Object getObject(String key);

    /***
     * 存储字符串
     * @param key
     * @param str
     * @param seconds
     */
    public void setString(String key, String str, int seconds);

    /***
     * 删除单个key
     * @param key
     */
   public void deleteRedisByKey(String key);
    /***
     * 删除集合key
     * @param keys
     */
    public void deleteRedisByKey(Collection keys);

    /***
     * 某个key加锁
     * @param key
     * @param seconds ，多少秒的锁时间
     * @return
     */
    public boolean redisLock(String key, int seconds);

    /***
     * 更新key过期时间
     * @param key
     * @param seconds
     * @param timeUnit
     * @return
     */
    public boolean expire(String key, int seconds, TimeUnit timeUnit);

    public boolean hset(String key, String field, String value);
    public String hget(String key, String field);
    public Long hdel(String key, String field);
    public Long hlen(String key);
}
