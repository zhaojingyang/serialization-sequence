package com.zealren.sequence.spring.boot.autoconfigue;

import com.zealren.sequence.formatter.SequenceFormatter;
import com.zealren.sequence.formatter.common.CommonSeqFormatter;
import com.zealren.sequence.generator.SequenceGenerator;
import com.zealren.sequence.generator.redisgenerator.RedisSequenceGeneratorImpl;
import com.zealren.sequence.generator.redisgenerator.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;


@Configuration
@ConditionalOnClass(value = {SequenceFormatter.class, SequenceGenerator.class})
@EnableConfigurationProperties(SequenceProperties.class)
@Configurable(autowire = Autowire.BY_NAME)
public class SequenceAutoConfiguration {

    @Value("${sequence.redis.database}")
    private int sequenceDatabase;

    @Value("${sequence.redis.host}")
    private String host;

    @Value("${sequence.redis.password}")
    private String password;

    @Value("${sequence.redis.port}")
    private int port;

    @Value("${sequence.redis.timeout}")
    private int timeout;

    @Value("${sequence.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${sequence.redis.pool.min-idle}")
    private int minIdle;

    @Value("${sequence.redis.pool.max-wait}")
    private long maxWait;

    @Bean("sequenceJedisConnectionFactory")
    @ConditionalOnMissingBean(name = "sequenceJedisConnectionFactory")
    public JedisConnectionFactory getConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(sequenceDatabase);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));//  connection timeout
        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());
        return factory;
    }


    @Bean(name = "sequenceStringRedisTemplate")
    @ConditionalOnMissingBean(name = "sequenceStringRedisTemplate")
    public StringRedisTemplate getForeRedisTemplate(@Qualifier("sequenceJedisConnectionFactory") JedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
        return stringRedisTemplate;
    }


    @Bean("sequenceJedisUtil")
    @ConditionalOnMissingBean(name = "sequenceJedisUtil")
    public JedisUtil getJedisUtil(@Qualifier("sequenceStringRedisTemplate") StringRedisTemplate sequenceStringRedisTemplate) {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setRedisTemplate(sequenceStringRedisTemplate);
        return jedisUtil;
    }

    @Bean("commonSequenceFormatter")
    @ConditionalOnMissingBean(name = "commonSequenceFormatter")
    public SequenceFormatter getCommonSequenceFormatter() {
        SequenceFormatter sequenceFormatter = new CommonSeqFormatter();
        return sequenceFormatter;
    }

    @Bean("redisSequenceGenerator")
    @DependsOn("sequenceJedisUtil")
    @ConditionalOnMissingBean(name = "redisSequenceGenerator")
    public SequenceGenerator getRedisSequenceGenerator(@Qualifier("sequenceJedisUtil") JedisUtil jedisUtil) {
        RedisSequenceGeneratorImpl sequenceGenerator = new RedisSequenceGeneratorImpl();
        sequenceGenerator.setSequenceJedisUtil(jedisUtil);
        return sequenceGenerator;
    }
}
