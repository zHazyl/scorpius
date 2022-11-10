package com.tnh.authservice.config;

import com.tnh.authservice.domain.User;
import com.tnh.authservice.dto.UserDTO;
import com.tnh.authservice.mapper.UserMapper;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration //The class must be marked with @Configuration
@EnableWebSecurity //Applies the configuration to the global WebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true) //Enables @RoleAllowed
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    //Registers the Keycloak authentication provider
    //The HttpSecurity object passed into the method configures all access rules
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
//        http.authorizeRequests().anyRequest().permitAll();
        http
                .cors()
                .and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().disable()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/users/authenticate").permitAll()
                .mvcMatchers(HttpMethod.POST, "/users/user").permitAll()
                .anyRequest().authenticated();
    }

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

//    @Autowired
//    public KeycloakClientRequestFactory keycloakClientRequestFactory;
//
//    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public KeycloakRestTemplate keycloakRestTemplate() {
//        return new KeycloakRestTemplate(keycloakClientRequestFactory);
//    }

}