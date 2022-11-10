package com.tnh.chatmessagesservice.security;

import com.tnh.chatmessagesservice.utils.security.SecurityUserDetails;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

public class SecurityUtils {

    public static Mono<String> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .cast(SecurityUserDetails.class)
                .map(SecurityUserDetails::getId);
    }


}
