package net.befriendme.dao;

import net.befriendme.entity.device.DeviceInfo;
import net.befriendme.entity.device.UserDevice;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class UserDeviceDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<String> getDeviceTokenOfUserFriends(String userId) {

        // Match the Friendship document based on a given criteria
        MatchOperation matchOperation = Aggregation.match(where("")
                .orOperator(
                        Criteria.where("requester._id").is(userId),
                        Criteria.where("recipient._id").is(userId)
                ));

        // Lookup to join UserDevice collection for requester
        LookupOperation lookupRequester = LookupOperation.newLookup()
                .from("userDevice")
                .localField("requester._id") // the field in the Friendship collection
                .foreignField("userId") // the field in the UserDevice collection
                .as("requesterDevice");

        // Lookup to join UserDevice collection for recipient
        LookupOperation lookupRecipient = LookupOperation.newLookup()
                .from("userDevice")
                .localField("recipient._id") // the field in the Friendship collection
                .foreignField("userId") // the field in the UserDevice collection
                .as("recipientDevice");

        // Perform the aggregation
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                lookupRequester,
                lookupRecipient
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "friendship", Document.class);

        return results
                .getMappedResults()
                .stream()
                .flatMap(document -> Stream.of(document.get("requester._id").toString(), document.get("requester._id").toString()))
                .filter(targetUserId -> !userId.equals(targetUserId))
                .distinct()
                .collect(toList());

    }

    public List<String> getDeviceTokenOfFollowers(String userId) {
        return null;
    }

    public List<String> getDeviceToken(List<String> userList) {

        Query query = new Query(
                Criteria
                .where("userId")
                .in(userList)
        );

        return mongoTemplate.find(query, UserDevice.class)
                .stream()
                .flatMap(userDevice -> userDevice.getDeviceTokenList().stream().map(DeviceInfo::getDeviceToken))
                .collect(Collectors.toList());
    }

}
