package net.befriendme.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class AuthUtils {

    public static String getEmail() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String sub = (String) authenticationToken.getTokenAttributes().get("sub");

        return sub.contains("@")
                ? sub
                : (String) authenticationToken.getTokenAttributes().get("email");
    }

}
