package net.befriendme.entity.device;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class UserDevice {


    private String userId;

    private List<DeviceInfo> deviceTokenList;

}
