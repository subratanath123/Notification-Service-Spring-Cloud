package net.befriendme.entity.device;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
public class DeviceInfo {

    private String deviceToken;
    private Instant lastActive;

    public DeviceInfo(String deviceToken, Instant lastActive) {
        this.deviceToken = deviceToken;
        this.lastActive = lastActive;
    }
}
