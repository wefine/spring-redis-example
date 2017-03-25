/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.springdata.redis.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Christoph Strobl
 */
@Configuration
@EnableRedisRepositories
public class ApplicationConfiguration {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(2);
        config.setMaxTotal(20);

        return config;
    }

    @Bean
    public RedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setUsePool(true);
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPoolConfig(jedisPoolConfig);

        // 设置数据库索引
        factory.setDatabase(5);

        return factory;
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory jedisConnectionFactory) {

        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);

        return template;
    }
}
