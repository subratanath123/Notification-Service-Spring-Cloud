package net.befriendme.api.rest.business;

import jakarta.validation.Valid;
import net.befriendme.entity.user.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/publish-event")
public class EventPublishController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, Event> redisTemplate;

    @PostMapping("/basic-information")
    public Event updateBasicInformation(@Valid @RequestBody Event event) {

        publishEventToRedis(event);

        return mongoTemplate.insert(event);
    }

    private void publishEventToRedis(Event event) {
        ObjectRecord<String, Event> record = StreamRecords.newRecord()
                .ofObject(event)
                .withStreamKey("event:" + event.getEventSource() + ":" + event.getDomain());

        redisTemplate.opsForStream().add(record);
    }

}
