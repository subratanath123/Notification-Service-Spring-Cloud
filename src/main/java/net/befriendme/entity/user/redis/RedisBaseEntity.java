package net.befriendme.entity.user.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.index.GeoIndexed;

import java.io.Serializable;

@Data
public abstract class RedisBaseEntity implements Serializable {

    private String mongoId; //mongo identifier

    @Id
    private String hashKey; //redis cache hashKey

    @GeoIndexed
    private Point location;
    private String name; //display name
    private String category;  //meta info about base entity
    private String collectionName; //collection name

    public RedisBaseEntity() {
    }

    public RedisBaseEntity(String mongoId, Point location,
                           String name, String category, String collectionName) {
        this(mongoId, mongoId, location, name, category, collectionName);
    }

    public RedisBaseEntity(String mongoId, String hashKey, Point location,
                           String name, String category, String collectionName) {

        this.mongoId = mongoId;
        this.hashKey = hashKey;
        this.location = location;
        this.name = name;
        this.category = category;
        this.collectionName = collectionName;
    }

}
