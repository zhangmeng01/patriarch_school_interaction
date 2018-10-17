package com.yjtoon.school.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

/**
 * 自定义springSession序列化对象
 */
public class RcJdkSerializationRedisSerializer implements RedisSerializer<Object> {
    private static final Logger LOGGER= LoggerFactory.getLogger(RcJdkSerializationRedisSerializer.class);
    private final Converter<Object, byte[]> serializer;
    private final Converter<byte[], Object> deserializer;

    /**
     *
     */
    public RcJdkSerializationRedisSerializer() {
        this(new SerializingConverter(), new DeserializingConverter());
    }

    /**
     *
     *
     * @param classLoader
     * @since 1.7
     */
    public RcJdkSerializationRedisSerializer(ClassLoader classLoader) {
        this(new SerializingConverter(), new DeserializingConverter(classLoader));
    }

    /**
     *
     * deserialize objects.
     *
     * @param serializer must not be {@literal null}
     * @param deserializer must not be {@literal null}
     * @since 1.7
     */
    public RcJdkSerializationRedisSerializer(Converter<Object, byte[]> serializer, Converter<byte[], Object> deserializer) {

        Assert.notNull(serializer, "Serializer must not be null!");
        Assert.notNull(deserializer, "Deserializer must not be null!");

        this.serializer = serializer;
        this.deserializer = deserializer;
    }
    /**
     * 人朝自定义反序列化接口
     */
    public Object deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try {
            return deserializer.convert(bytes);
        } catch (Exception ex) {
//            LOGGER.info("",ex);
//            throw new SerializationException("Cannot deserialize", ex);
            return bytes;
        }
    }

    public byte[] serialize(Object object) {
        if (object == null) {
            return new byte[0];
        }
        try {
            return serializer.convert(object);
        } catch (Exception ex) {
            throw new SerializationException("Cannot serialize", ex);
        }
    }
}