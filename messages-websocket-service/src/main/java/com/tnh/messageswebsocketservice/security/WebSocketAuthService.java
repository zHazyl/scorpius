package com.tnh.messageswebsocketservice.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tnh.messageswebsocketservice.utils.jwt.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebSocketAuthService {

    private final JWTUtils jwtUtils;

    public WebSocketAuthService(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    public UsernamePasswordAuthenticationToken attemptAuthentication(String authorizationHeaderValue) {
        try {
            var token = authorizationHeaderValue.replace(jwtUtils.getJwtConfig().getTokenPrefix(), "");
            return jwtUtils.getAuthentication(token);
        } catch (JWTDecodeException | TokenExpiredException ex) {
            log.error("Invalid token");
        }
        return null;
    }
}
