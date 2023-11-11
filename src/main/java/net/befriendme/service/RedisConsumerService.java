package net.befriendme.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.stereotype.Component;

@Component
public class RedisConsumerService {

    private final Logger log = LoggerFactory.getLogger(RedisConsumerService.class);

    public void createConsumerGroupIfNotExists(RedisConnectionFactory redisConnectionFactory, String streamKey, String groupName) {
        try {
            try {
                redisConnectionFactory.getConnection().streamCommands()
                        .xGroupCreate(streamKey.getBytes(),
                                groupName,
                                ReadOffset.from("0-0"), true);

                log.info("Consumer Group {} created with stream key {}", groupName, streamKey);

            } catch (RedisSystemException exception) {
                log.warn(exception.getCause().getMessage());
            }
        } catch (RedisSystemException ex) {
            log.error(ex.getMessage());
        }
    }
}
