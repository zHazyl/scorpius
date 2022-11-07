package com.tnh.authservice.web.rest;

import com.tnh.authservice.config.KeycloakProvider;
import com.tnh.authservice.dto.UserDTO;
import com.tnh.authservice.mapper.UserMapper;
import com.tnh.authservice.security.model.AuthRequestModel;
import com.tnh.authservice.service.KeycloakAdminClientService;
import com.tnh.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final KeycloakAdminClientService keycloakAdminClientService;
    private final KeycloakProvider keycloakProvider;

    // fix mapper in pom.xml
    public UserController(UserService userService,
                          UserMapper userMapper,
                          KeycloakAdminClientService keycloakAdminClientService,
                          KeycloakProvider keycloakProvider) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.keycloakAdminClientService = keycloakAdminClientService;
        this.keycloakProvider = keycloakProvider;
//        this.userSender = userSender;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user")
    public ResponseEntity<UserDTO> createNewUser(@Valid @RequestBody UserDTO userDTO,
                                                 UriComponentsBuilder uriComponentsBuilder) {
        keycloakAdminClientService.createKeycloakUser(userDTO);
        var user = userService.createUser(userDTO.getUsername(), userDTO.getPassword(),
                userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName());

//        userSender.send(userMapper.mapToUserDTO(user));
        userMapper.mapToUserDTO(user);

        var location = uriComponentsBuilder.path("/users/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(userMapper.mapToUserDTOWithoutActivationKey(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") String userId) {
        return ResponseEntity.ok(userMapper.mapToUserDTO(userService.findUserById(userId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> editUser(@PathVariable("id") String userId, @RequestBody UserDTO userDTO) {
        var user = userService.modifyUser(userId, userDTO.getFirstName(), userDTO.getLastName());
        return ResponseEntity.ok(userMapper.mapToUserDTO(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody AuthRequestModel authRequestModel) {
        Keycloak keycloak = keycloakProvider.newKeycloakBuilderWithPasswordCredentials(authRequestModel.getUsername(), authRequestModel.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }
    }

}
