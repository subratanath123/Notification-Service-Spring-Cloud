package net.befriendme.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PushNotificationResponse {

    private int status;
    private String message;

    public PushNotificationResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
