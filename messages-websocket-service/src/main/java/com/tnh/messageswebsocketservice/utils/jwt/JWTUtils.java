package com.tnh.messageswebsocketservice.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tnh.messageswebsocketservice.utils.security.SecurityUserDetailsImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

import static com.tnh.messageswebsocketservice.utils.jwt.JWTConstants.AUTHORITIES_KEY;
import static com.tnh.messageswebsocketservice.utils.jwt.JWTConstants.USERNAME_KEY;


public class JWTUtils {

    private final Algorithm sign;
    private final JWTConfig jwtConfig;

    public JWTUtils(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.sign = Algorithm.HMAC512(jwtConfig.getSecret());
    }

    public boolean isValidAuthorizationHeaderValue(String authHeaderValue) {
        return StringUtils.isNotBlank(authHeaderValue)
                && authHeaderValue.contains(jwtConfig.getTokenPrefix())
                && StringUtils.isNotBlank(authHeaderValue.replace(jwtConfig.getTokenPrefix(), ""));
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {

        var decodedJWT = JWT
                .require(sign)
                .build()
                .verify(token);

        if (decodedJWT != null) {
            var authorities = decodedJWT.getClaim(AUTHORITIES_KEY)
                    .asList(String.class)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            var userId = decodedJWT.getSubject();
            var username = decodedJWT.getClaim(USERNAME_KEY).asString();
            var securityUser = new SecurityUserDetailsImpl(userId, username, "", authorities);

            return new UsernamePasswordAuthenticationToken(securityUser, token, authorities);

        }
        return null;
    }

    public JWTConfig getJwtConfig() {
        return jwtConfig;
    }
}
