package com.zfile.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 使用StringRedisSerializer序列化key
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        // 使用JSON序列化value
        template.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        template.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        
        template.afterPropertiesSet();
        return template;
    }
    
    /**
     * FastJSON序列化器
     */
    static class FastJsonRedisSerializer<T> implements org.springframework.data.redis.serializer.RedisSerializer<T> {
        
        private final Class<T> clazz;
        
        public FastJsonRedisSerializer(Class<T> clazz) {
            super();
            this.clazz = clazz;
        }
        
        @Override
        public byte[] serialize(T t) {
            if (t == null) {
                return new byte[0];
            }
            return com.alibaba.fastjson2.JSON.toJSONString(t).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        
        @Override
        public T deserialize(byte[] bytes) {
            if (bytes == null || bytes.length <= 0) {
                return null;
            }
            String str = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
            return com.alibaba.fastjson2.JSON.parseObject(str, clazz);
        }
    }
}