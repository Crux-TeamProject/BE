package com.project.crux.chat.config;

import com.project.crux.chat.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {
    private static final String CHANNEL_TOPIC = "chatroom";
    private static final String REDIS_SUBSCRIBER_DEFAULT_METHOD = "sendMessage";

    // chat server만 사용하기 위해 주석처리
//    @Value("${spring.redis.host}")
//    private String redisHostName;
//
//    @Value("${spring.redis.port}")
//    private int redisPort;

    /**
     * 단일 Topic 사용을 위한 Bean 설정
     */
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(CHANNEL_TOPIC);
    }

    // chat server만 사용하기 위해 주석처리
//    @Bean
//    public RedisConnectionFactory lettuceConnectionFactory() {
//        return new LettuceConnectionFactory(redisHostName, redisPort);
//    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber redisSubscriber){
        return new MessageListenerAdapter(redisSubscriber,REDIS_SUBSCRIBER_DEFAULT_METHOD);
    }

    /**
     * redis에 발행(publish)된 메시지 처리를 위한 리스너 설정
     */
//    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory,
                                                              MessageListenerAdapter listenerAdapter,
                                                              ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    /**
     * redisTemplate 설정
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }
}