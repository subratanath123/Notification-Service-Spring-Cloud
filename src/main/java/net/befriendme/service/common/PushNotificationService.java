package net.befriendme.service.common;

import net.befriendme.dto.PushNotificationRequest;
import net.befriendme.service.fcm.GoogleFCMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PushNotificationService implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Autowired
    private GoogleFCMService googleFcmService;

    @Override
    public void sendPushNotificationToTopic(Map<String, String> payloadDataMap, PushNotificationRequest request) {
        try {
            googleFcmService.sendPushMessageWithoutDataToTopic(payloadDataMap, request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void sendPushNotificationToDevice(Map<String, String> payloadDataMap, PushNotificationRequest request) {
        try {
            googleFcmService.sendPushMessageToDevice(payloadDataMap, request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}

