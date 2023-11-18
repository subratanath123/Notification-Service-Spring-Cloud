package net.befriendme.entity.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document
@Data
public class Event implements Serializable {

    @Id
    private String eventId;

    private String resourceId;

    private String message;

    private String externalUrl;

    private String eventType;

    private String domain;

    @NotEmpty
    @NotNull
    private String eventSource;

    @NotEmpty
    @NotNull
    private String version;

    @NotEmpty
    @NotNull
    private Map<String, String> payload;
    private Map<String, String> metadata;

    @NotEmpty
    private List<Audience> audienceList;
    private List<String> targetUserList;

    public Event() {
        this.eventId = UUID.randomUUID().toString();
    }
}
