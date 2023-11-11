package net.befriendme.entity.user.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Document
@Data
public class Event implements Serializable {

    @Id
    private String eventId;

    @NotEmpty
    @NotNull
    private String eventType;

    @NotEmpty
    @NotNull
    private String domain;

    @NotEmpty
    @NotNull
    private String eventSource;

    @NotEmpty
    @NotNull
    private Instant timestamp;

    @NotEmpty
    @NotNull
    private String version;

    @NotEmpty
    @NotNull
    private Map<String, Object> payload;
    private Map<String, String> metadata;

    public Event() {
        this.eventId = UUID.randomUUID().toString();
    }
}
