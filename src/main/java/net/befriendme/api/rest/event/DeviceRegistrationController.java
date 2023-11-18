package net.befriendme.api.rest.event;

import jakarta.validation.Valid;
import net.befriendme.dao.UserDao;
import net.befriendme.dto.SingleUserDevice;
import net.befriendme.entity.device.DeviceInfo;
import net.befriendme.entity.device.UserDevice;
import net.befriendme.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
@RequestMapping("/v1/")
public class DeviceRegistrationController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserDao userDao;

    @PostMapping("/register-device")
    public SingleUserDevice publish(@Valid @RequestBody SingleUserDevice userDevice) {
        String email = AuthUtils.getEmail();

        String userId = userDao.getUserId(email);

        Query query = new Query();
        query.addCriteria(where("userId").is(userId));

        addDeviceToken(userDevice, userId, query);

        return userDevice;
    }

    private void addDeviceToken(SingleUserDevice userDevice, String userId, Query query) {
        // Check if the UserDevice document exists
        boolean userDeviceExists = mongoTemplate.exists(query, UserDevice.class);

        if (!userDeviceExists) {
            // Create a new UserDevice document
            UserDevice newUserDevice = new UserDevice();
            newUserDevice.setUserId(userId);
            newUserDevice.setDeviceTokenList(List.of(new DeviceInfo(userDevice.getDeviceToken(), new Date().toInstant())));

            mongoTemplate.insert(newUserDevice);

        } else {
            // Update the existing UserDevice document
            Update update = new Update();
            update.push("deviceTokenList", new DeviceInfo(userDevice.getDeviceToken(), new Date().toInstant()));

            mongoTemplate.updateFirst(query, update, UserDevice.class);
        }
    }

}
