package net.befriendme.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StreamCleanupTaskService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 3600000/8) // every hour/8
    public void cleanOldStreams() {
        redisTemplate.opsForStream().trim("event:push:befriendme", 100, false);
    }
}
