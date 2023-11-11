package net.befriendme.entity.idp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google")
public class GoogleIdpConfig extends IdpConfig{
    
}
