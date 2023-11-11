package net.befriendme.config;

import net.befriendme.entity.idp.AppleIdpConfig;
import net.befriendme.entity.idp.BeFriendMeIdpConfig;
import net.befriendme.entity.idp.GoogleIdpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;

@Configuration
public class JwtIssuerAuthenticationManagerResolverConfig {

    @Autowired
    private BeFriendMeIdpConfig beFriendMeIdpConfig;

    @Autowired
    private GoogleIdpConfig googleIdpConfig;

    @Autowired
    private AppleIdpConfig appleIdpConfig;

    @Bean
    public JwtIssuerAuthenticationManagerResolver authenticationManagerResolver() {
        return new JwtIssuerAuthenticationManagerResolver(
                beFriendMeIdpConfig.getIssuerUrl(),
                googleIdpConfig.getIssuerUrl(),
                appleIdpConfig.getIssuerUrl()
        );
    }
}
