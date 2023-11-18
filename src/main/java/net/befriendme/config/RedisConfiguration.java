package net.befriendme.config;

import net.befriendme.service.redis.RedisConsumerService;
import net.befriendme.service.redis.RedisEventMessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.data.redis.stream.Subscription;

import java.net.UnknownHostException;
import java.time.Duration;

import static java.net.InetAddress.getLocalHost;

@Configuration
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.username}")
    private String redisUsername;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Autowired
    private RedisConsumerService redisConsumerService;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setUsername(redisUsername);
        config.setPassword(redisPassword);

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public Subscription beFriendMeRedisStreamSubscription(RedisConnectionFactory connectionFactory) throws UnknownHostException {
        String streamKey = "event:push:befriendme";
        redisConsumerService.createConsumerGroupIfNotExists(connectionFactory, streamKey, "notification-server-group-1");

        StreamOffset<String> streamOffset = StreamOffset.create(streamKey, ReadOffset.lastConsumed());

        StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
                StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofMillis(100))
                        .targetType(String.class)
                        .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
                StreamMessageListenerContainer
                        .create(connectionFactory, options);

        Subscription subscription =
                container.receive(Consumer.from("notification-server-group-1", getLocalHost().getHostName()),
                        streamOffset, beFriendMeMessageEventProcessor());

        container.start();
        return subscription;
    }

    @Bean
    public StreamListener<String, ObjectRecord<String, String>> beFriendMeMessageEventProcessor() {
        return new RedisEventMessageProcessor();
    }

}