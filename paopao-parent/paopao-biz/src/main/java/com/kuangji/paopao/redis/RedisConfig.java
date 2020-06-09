package com.kuangji.paopao.redis;

import java.io.IOException;
import java.time.Duration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.kuangji.paopao.redis.util.RedisUtils;

@Configuration
@EnableCaching // 开启注解
public class RedisConfig extends CachingConfigurerSupport {
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {

		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(10))
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()))
				.disableCachingNullValues();
		RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(factory);
		return builder.transactionAware().cacheDefaults(config).build();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setConnectionFactory(factory);
		return redisTemplate;
	}

	/**
	 * 注入封装RedisTemplate
	 * 
	 * @param redisTemplate
	 * @return
	 */
	@Bean(name = "redisUtil")
	public RedisUtils redisUtil(RedisTemplate<String, Object> redisTemplate) {
		RedisUtils redisUtil = new RedisUtils();
		redisUtil.setRedisTemplate(redisTemplate);
		return redisUtil;
	}

	/** 使用默认的工厂初始化redis操作模板 */
	@Bean
	public StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer() {
		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
		return redisMessageListenerContainer;

	}

//	/* 监听过期key */
//	@Bean
//	public KeyExpiredListener keyExpiredListener() {
//		return new KeyExpiredListener(this.redisMessageListenerContainer());
//	}

	@Bean
	public RedissonClient redisson() throws IOException {
		// 本例子使用的是yml格式的配置文件，读取使用Config.fromYAML，如果是Json文件，则使用Config.fromJSON
		Config config = Config.fromYAML(RedisConfig.class.getClassLoader().getResource("redisson.yml"));
		return Redisson.create(config);
	}
}
