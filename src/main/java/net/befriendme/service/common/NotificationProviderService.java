package net.befriendme.service.common;

import net.befriendme.dto.PushNotificationRequest;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface NotificationProviderService {
    void sendPushMessageToDevice(Map<String, String> data, PushNotificationRequest request) throws InterruptedException, ExecutionException;

    void sendPushMessageWithoutDataToTopic(Map<String, String> data, PushNotificationRequest request) throws InterruptedException, ExecutionException;
}
