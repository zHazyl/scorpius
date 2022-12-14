package com.tnh.chatmessagesservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;


@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveAuthenticationManager jwtAuthenticationManager,
                                                         ServerAuthenticationConverter jwtAuthenticationConverter) {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();
        http.requestCache().requestCache(NoOpServerRequestCache.getInstance());
        http.cors();
        http.csrf().disable();

        http.addFilterAt(authenticationWebFilter(jwtAuthenticationManager, jwtAuthenticationConverter),
                SecurityWebFiltersOrder.AUTHENTICATION);

        http.authorizeExchange()
                .anyExchange().authenticated();

        return http.build();
    }


    @Bean
    public AuthenticationWebFilter authenticationWebFilter(ReactiveAuthenticationManager jwtAuthenticationManager,
                                                           ServerAuthenticationConverter jwtAuthenticationConverter) {
        var authenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);
        return authenticationWebFilter;
    }

//    @Bean
//    public JWTConfig jwtConfig() {
//        return new JWTConfig();
//    }
//
//    @Bean
//    public JWTUtils jwtUtils() {
//        return new JWTUtils(jwtConfig());
//    }


}
