package com.tnh.messageswebsocketservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final BearerTokenAuthenticator bearerTokenAuthenticator;

    public AuthChannelInterceptor(BearerTokenAuthenticator bearerTokenAuthenticator) {
        this.bearerTokenAuthenticator = bearerTokenAuthenticator;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            var authorizationHeaderValue = (String) accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            final var authenticationToken = bearerTokenAuthenticator.authenticate(authorizationHeaderValue);

            if (authenticationToken != null)
                accessor.setUser(authenticationToken);
        }

        return message;
    }
}
