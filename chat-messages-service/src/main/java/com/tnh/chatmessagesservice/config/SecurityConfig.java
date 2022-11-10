package com.tnh.chatmessagesservice.config;

import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;


@EnableWebFluxSecurity
@Configuration //The class must be marked with @Configuration
@EnableWebSecurity //Applies the configuration to the global WebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true) //Enables @RoleAllowed
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    //Registers the Keycloak authentication provider
    //The HttpSecurity object passed into the method configures all access rules
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
////        http.authorizeRequests().anyRequest().permitAll();
//        http
//                .cors()
//                .and().csrf().disable()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .httpBasic().disable()
//                .authorizeRequests()
//                .mvcMatchers(HttpMethod.POST, "/users/authenticate").permitAll()
//                .mvcMatchers(HttpMethod.POST, "/users/user").permitAll()
//                .anyRequest().authenticated();
//    }

    // Defines the session authentication strategy
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider =
                keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
                new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    // Defines the session authentication strategy
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private ResolverConfig resolverConfig;

    @Bean
    public KeycloakDeployment getKeycloakDeployment() {
        return resolverConfig.KeycloakConfigResolver().resolve(null);
    }

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

//    @Autowired
//    public KeycloakClientRequestFactory keycloakClientRequestFactory;
//
//    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public KeycloakRestTemplate keycloakRestTemplate() {
//        return new KeycloakRestTemplate(keycloakClientRequestFactory);
//    }

}
