package net.befriendme.dao;

import net.befriendme.entity.user.redis.RedisBaseEntity;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisBaseEntityDao redisBaseEntityDao;

    public String getUserId(String email) {

        if (redisBaseEntityDao.existsById(email)) {
            return redisBaseEntityDao.findById(email).map(RedisBaseEntity::getMongoId).orElse(null);

        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(email));
            query.fields().include("id");

            Document userDocument = mongoTemplate.findOne(query, Document.class, "user");
            assert userDocument != null;
            return userDocument.getObjectId("_id").toString();
        }
    }
}
