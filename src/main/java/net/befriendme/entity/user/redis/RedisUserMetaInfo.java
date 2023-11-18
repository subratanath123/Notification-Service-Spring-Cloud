package net.befriendme.entity.user.redis;

import lombok.Data;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("user")
public class RedisUserMetaInfo extends RedisBaseEntity implements Serializable {

    private String email;

    public RedisUserMetaInfo() {
    }

    public RedisUserMetaInfo(String email, String mongoId, String name) {
        super(mongoId, email, null, name, "user", "user");
        this.email = email;
    }

    public RedisUserMetaInfo(String email, String mongoId, Point location, String name, String category, String type) {
        super(mongoId, email, location, name, category, type);
        this.email = email;
    }
}
