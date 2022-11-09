package com.tnh.messageswebsocketservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tnh.messageswebsocketservice.config.KeycloakProvider;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebSocketAuthService {

    private final KeycloakProvider keycloakProvider;

    public WebSocketAuthService(KeycloakProvider keycloakProvider) {
        this.keycloakProvider = keycloakProvider;
    }

    public KeycloakAuthenticationToken attemptAuthentication(String authorizationHeaderValue) {

        try {
            var token = authorizationHeaderValue.replace("Bearer ", "");
            var decode = JWT.decode(token);
            var id = decode.getSubject();
            var usersResource = keycloakProvider.getInstance().realm(keycloakProvider.getRealm()).users();
//            var username = usersResource.toRepresentation().getUsername();

        } catch (JWTDecodeException | TokenExpiredException ex) {
            log.error("Invalid token");
        }
        return null;
    }


}
