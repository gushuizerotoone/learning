package com.gulewang.backend.config;

import com.gulewang.backend.session.SessionInfo;
import com.gulewang.stock.api.bean.StockBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by Thomas on 17/4/8.
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

  public static final String PREFIX_CORE_STOCK = "core:stock:";
  public static final String PREFIX_CORE_SESSION = "core:session:";


  @Autowired
  JedisConnectionFactory jedisConnectionFactory; // spring boot help to inject

  @Bean
  public RedisTemplate<String, SessionInfo> sessionRedisTemplate() {
    RedisTemplate<String, SessionInfo> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer(SessionInfo.class));
    return template;
  }

  @Bean
  public RedisTemplate<String, Object> myRedisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
    return template;
  }

//  @Bean
//  public RedisCacheManager redisCacheManager() {
//    RedisCacheManager cacheManager = new RedisCacheManager(myRedisTemplate());
////    cacheManager.setCacheNames();
//    cacheManager.setDefaultExpiration(30000);
//    return cacheManager;
//  }
//
//  @Bean
//  public KeyGenerator keyGenerator() {
//    return (o, method, objects) -> {
//      // This will generate a unique key of the class name, the method name,
//      // and all method parameters appended.
//      StringBuilder sb = new StringBuilder();
//      sb.append(o.getClass().getName());
//      sb.append(method.getName());
//      for (Object obj : objects) {
//        sb.append(obj.toString());
//      }
//      return sb.toString();
//    };
//  }

}