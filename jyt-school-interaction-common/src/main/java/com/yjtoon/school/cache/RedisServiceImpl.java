package com.yjtoon.school.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Description： redis 操作实现
 * @author: SHENZL
 * @since: 2017/11/9 11:46
 */
@Component
public class RedisServiceImpl implements IRedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    RedisTemplate<String,String> redisTemplate;


    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setString(String key, String value, int seconds) {
        if (seconds <= 0)
//            redisTemplate.opsForValue().set(new String(SerializationUtils.serialize(key)),new String(SerializationUtils.serialize(value)) );
            redisTemplate.opsForValue().set(key,value);
        else
            redisTemplate.opsForValue().set(key,value, seconds);
    }

    @Override
    public void deleteRedisByKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void deleteRedisByKey(Collection keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public boolean redisLock(String key, int seconds) {
        if (seconds <= 0) {
            seconds = 21600;
        }
        boolean result = false;
        if (key == null)
            return result;
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime + seconds * 1000;
        if (redisTemplate.opsForValue().setIfAbsent(encode(key), String.valueOf(expireTime))) {
            result = true;
            this.expire(key,seconds, TimeUnit.SECONDS);
        } else {
            //防止死锁
            Long oldExpireTime = Long.valueOf(getString(encode(key)));
            if ((oldExpireTime != null) && (currentTime > oldExpireTime.longValue())) {
                deleteRedisByKey(encode(key));
                if (redisTemplate.opsForValue().setIfAbsent(encode(key), String.valueOf(expireTime))) {
                    result = true;
                    this.expire(key,seconds, TimeUnit.SECONDS);
                }
            }
        }
        return result;
    }

    @Override
    public boolean expire(String key, int seconds, TimeUnit timeUnit) {
        timeUnit = timeUnit == null ? timeUnit.SECONDS : timeUnit;
        return redisTemplate.expire(encode(key),seconds,timeUnit);
    }

    @Override
    public boolean hset(String key, String field, String value) {
        return redisTemplate.getConnectionFactory().getConnection().hSet(key.getBytes(), field.getBytes(), value.getBytes());
    }

    @Override
    public String hget(String key, String field) {
        byte[] result = redisTemplate.getConnectionFactory().getConnection().hGet(key.getBytes(), field.getBytes());
        return new String(result);
    }

    @Override
    public Long hdel(String key, String field) {
        return redisTemplate.getConnectionFactory().getConnection().hDel(key.getBytes(), field.getBytes());
    }

    @Override
    public Long hlen(String key) {
        return redisTemplate.getConnectionFactory().getConnection().hDel(key.getBytes());
    }


    public static String encode(String str) {
        String ret = null;
        if (str != null) {
            try {
                ret = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    @Override
    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
