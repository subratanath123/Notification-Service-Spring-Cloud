package net.befriendme.service.fcm;

import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.befriendme.dto.PushNotificationRequest;
import net.befriendme.service.common.NotificationProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class GoogleFCMService implements NotificationProviderService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleFCMService.class);

    @Override
    public void sendPushMessageToDevice(Map<String, String> data, PushNotificationRequest request) throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToDevice(data, request);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);

        logger.info("Sent message with data. Device: " + request.getDeviceToken() + ", " + response + " msg " + jsonOutput);
    }

    @Override
    public void sendPushMessageWithoutDataToTopic(Map<String, String> data, PushNotificationRequest request) throws InterruptedException, ExecutionException {

        Message message = getPreconfiguredMessageWithoutDataToTopic(data, request);
        String response = sendAndGetResponse(message);

        logger.info("Sent message without data. Topic: " + request.getTopic() + ", " + response);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging
                .getInstance()
                .sendAsync(message)
                .get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(
                        AndroidNotification
                                .builder()
                                .setTag(topic)
                                .build()
                )
                .build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps
                        .builder()
                        .setCategory(topic)
                        .setThreadId(topic)
                        .build())
                .build();
    }

    private Message getPreconfiguredMessageToDevice(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                .putAllData(data)
                .setToken(request.getDeviceToken())
                .build();
    }

    private Message getPreconfiguredMessageWithoutDataToTopic(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                .putAllData(data)
                .setTopic(request.getTopic())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());

        return Message
                .builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
                        Notification
                                .builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getMessage())
                                .setImage(null)
                                .build()
                );
    }
}
