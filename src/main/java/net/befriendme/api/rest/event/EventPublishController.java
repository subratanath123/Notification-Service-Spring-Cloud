package net.befriendme.api.rest.event;

import jakarta.validation.Valid;
import net.befriendme.entity.event.Event;
import net.befriendme.service.common.EventToPushMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.befriendme.utils.SerializationUtils.getSerializedJson;

@RestController
@RequestMapping("/v1")
public class EventPublishController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private EventToPushMessagingService eventToPushMessagingService;

    @PostMapping("/publish-event")
    public Event publish(@Valid @RequestBody Event event) {

        publishEventToRedis(event, "event:push:" + event.getEventSource());

        return mongoTemplate.insert(event);
    }

    private void publishEventToRedis(Event event, String streamKey) {
        ObjectRecord<String, String> record = StreamRecords.newRecord()
                .ofObject(getSerializedJson(event))
                .withStreamKey(streamKey);

        redisTemplate.opsForStream().add(record);
    }

}
