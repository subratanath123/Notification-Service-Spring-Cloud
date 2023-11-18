package net.befriendme.service.common;

import net.befriendme.dto.PushNotificationRequest;

import java.util.Map;

public interface NotificationService {

    void sendPushNotificationToTopic(Map<String, String> payloadDataMap, PushNotificationRequest request);
    void sendPushNotificationToDevice(Map<String, String> payloadDataMap, PushNotificationRequest request);

}
