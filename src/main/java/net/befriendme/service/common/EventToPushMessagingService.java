package net.befriendme.service.common;

import net.befriendme.dao.UserDeviceDao;
import net.befriendme.dto.PushNotificationRequest;
import net.befriendme.entity.event.Audience;
import net.befriendme.entity.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventToPushMessagingService {

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private UserDeviceDao userDeviceDao;

    public void sendEventAsPushNotification(Event event) {

        Map<String, String> payloadData = getPayloadData(event);

        PushNotificationRequest.Builder pushNotificationRequestBuilder = getPushNotificationRequest(event);

        event.getAudienceList().forEach(audience -> {

            if (Audience.PUBLIC.equals(audience)) {

                sendPushNotificationDestinationToTopic(pushNotificationRequestBuilder, payloadData, "public");

            } else if (Audience.TARGET_USER.equals(audience)) {

                sendPushNotificationDestinationDevice(pushNotificationRequestBuilder, payloadData, userDeviceDao.getDeviceToken(event.getTargetUserList()));

            } else if (Audience.FRIEND.equals(audience)) {

                for (String userId : event.getTargetUserList()) {
                    sendPushNotificationDestinationDevice(pushNotificationRequestBuilder, payloadData, userDeviceDao.getDeviceTokenOfUserFriends(userId));
                }

            } else if (Audience.FOLLOWER.equals(audience)) {

                for (String userId : event.getTargetUserList()) {
                    sendPushNotificationDestinationDevice(pushNotificationRequestBuilder, payloadData, userDeviceDao.getDeviceTokenOfFollowers(userId));
                }

            }

        });
    }

    private void sendPushNotificationDestinationDevice(PushNotificationRequest.Builder pushNotificationRequestBuilder,
                                                       Map<String, String> payloadData,
                                                       List<String> deviceTokenList) {
        if (!CollectionUtils.isEmpty(deviceTokenList)) {

            deviceTokenList.forEach(deviceToken -> {
                pushNotificationRequestBuilder.setDeviceToken(deviceToken);
                pushNotificationService.sendPushNotificationToDevice(payloadData, pushNotificationRequestBuilder.build());
            });

        }
    }

    private void sendPushNotificationDestinationToTopic(PushNotificationRequest.Builder notificationRequestBuilder,
                                                        Map<String, String> payloadData,
                                                        String topic) {

        notificationRequestBuilder.setTopic(topic);
        pushNotificationService.sendPushNotificationToDevice(payloadData, notificationRequestBuilder.build());
    }


    private PushNotificationRequest.Builder getPushNotificationRequest(Event event) {
        return new PushNotificationRequest
                .Builder()
                .setTitle(event.getDomain())
                .setMessage(event.getMessage());
    }

    private Map<String, String> getPayloadData(Event event) {
        Map<String, String> pushData = new HashMap<>();

        pushData.put("eventId", event.getEventId());
        pushData.put("resourceId", event.getResourceId());
        pushData.put("domain", event.getDomain());
        pushData.put("message", event.getMessage());
        pushData.put("externalUrl", event.getExternalUrl());

        return pushData;
    }

}
