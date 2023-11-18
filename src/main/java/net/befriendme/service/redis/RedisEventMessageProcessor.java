package net.befriendme.service.redis;

import net.befriendme.entity.event.Event;
import net.befriendme.service.common.EventToPushMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

import static net.befriendme.utils.SerializationUtils.getDeserializedObject;

@Service
public class RedisEventMessageProcessor implements StreamListener<String, ObjectRecord<String, String>> {

    private Logger log = LoggerFactory.getLogger(RedisEventMessageProcessor.class);

    @Autowired
    private RedisTemplate<String, String> redisBaseEntityRedisTemplate;

    @Autowired
    private EventToPushMessagingService eventToPushMessagingService;

    @Override
    public void onMessage(ObjectRecord<String, String> record) {

        eventToPushMessagingService.sendEventAsPushNotification(getDeserializedObject(record.getValue(), Event.class));

        redisBaseEntityRedisTemplate
                .opsForStream()
                .acknowledge("notification-server-group-1", record);
    }
}