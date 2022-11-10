package com.tnh.chatmessagesservice.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tnh.chatmessagesservice.config.BearerTokenAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JWTAuthenticationManager implements ReactiveAuthenticationManager {

    private BearerTokenAuthenticator bearerTokenAuthenticator;

    public JWTAuthenticationManager(BearerTokenAuthenticator bearerTokenAuthenticator) {
        this.bearerTokenAuthenticator = bearerTokenAuthenticator;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.just(authentication)
                .map(Authentication::getCredentials)
                .cast(String.class)
                .flatMap(token -> {
                    log.debug("Authenticate {}" , token);
                    try {
                        return Mono.just(bearerTokenAuthenticator.authenticate(token));
                    } catch (JWTDecodeException | TokenExpiredException ex) {
                        log.error("Decode token exception");
                        return Mono.empty();
                    }
                });
    }
}
