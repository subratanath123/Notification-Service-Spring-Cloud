package net.befriendme.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class PushNotificationRequest implements Serializable {

    private String title;
    private String message;
    private String topic;
    private String deviceToken;

    private PushNotificationRequest(Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.topic = builder.topic;
        this.deviceToken = builder.deviceToken;
    }

    public static class Builder {
        private String title;
        private String message;
        private String topic;
        private String deviceToken;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setTopic(String topic) {
            this.topic = topic;
            this.deviceToken = null;
            return this;
        }

        public Builder setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
            this.topic = null;
            return this;
        }

        public PushNotificationRequest build() {
            return new PushNotificationRequest(this);
        }
    }
}


