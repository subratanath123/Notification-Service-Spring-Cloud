package net.befriendme.dao;

import net.befriendme.entity.user.redis.RedisUserMetaInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedisBaseEntityDao extends CrudRepository<RedisUserMetaInfo, String> {

    List<RedisUserMetaInfo> findByLocationNear(Point point, Distance distance, Pageable pageable);

    List<RedisUserMetaInfo> findByLocationWithin(Circle circle);

}
