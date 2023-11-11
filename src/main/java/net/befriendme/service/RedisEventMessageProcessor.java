package net.befriendme.service;

import net.befriendme.entity.user.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class RedisEventMessageProcessor implements StreamListener<String, ObjectRecord<String, Event>> {

    private Logger log = LoggerFactory.getLogger(RedisEventMessageProcessor.class);

    @Autowired
    private RedisTemplate<String, Event> redisBaseEntityRedisTemplate;

    @Override
    public void onMessage(ObjectRecord<String, Event> record) {
        redisBaseEntityRedisTemplate.opsForStream().acknowledge("notification-server-group-1", record);
    }
}