package net.befriendme.dto;

import lombok.Data;
import net.befriendme.entity.device.DeviceInfo;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Document
@Data
public class SingleUserDevice implements Serializable {

    private String deviceToken;

}
