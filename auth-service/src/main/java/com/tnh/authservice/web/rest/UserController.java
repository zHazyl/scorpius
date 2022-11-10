package com.tnh.authservice.web.rest;

import com.auth0.jwt.JWT;
import com.tnh.authservice.config.KeycloakProvider;
import com.tnh.authservice.dto.UserDTO;
import com.tnh.authservice.mapper.UserMapper;
import com.tnh.authservice.messaging.sender.UserSender;
import com.tnh.authservice.security.model.AuthRequestModel;
import com.tnh.authservice.security.model.TokenResponse;
import com.tnh.authservice.service.KeycloakAdminClientService;
import com.tnh.authservice.service.UserService;
import com.tnh.authservice.utils.SecurityUtils;
import com.tnh.authservice.utils.exception.InvalidDataException;
//import com.tnh.authservice.utils.security.SecurityUserDetails;
import com.tnh.authservice.web.model.ChangePassRequest;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserSender userSender;
    private final KeycloakAdminClientService keycloakAdminClientService;
    private final KeycloakProvider keycloakProvider;

    // fix mapper in pom.xml
    public UserController(UserService userService,
                          UserMapper userMapper,
                          UserSender userSender,
                          KeycloakAdminClientService keycloakAdminClientService,
                          KeycloakProvider keycloakProvider) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userSender = userSender;
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
        Keycloak keycloak = keycloakProvider.newKeycloakBuilderWithPasswordCredentials(userDTO.getUsername(), userDTO.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        String token = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            token = accessTokenResponse.getToken();
        } catch (Exception e) {

        }
        var decode = JWT.decode(token);
        var id = decode.getSubject();
        user.setId(UUID.fromString(id));
        userSender.send(userMapper.mapToUserDTO(user));
//        userMapper.mapToUserDTO(user);

        var location = uriComponentsBuilder.path("/users/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).body(userMapper.mapToUserDTOWithoutActivationKey(user));
    }

//    @PatchMapping("/activate")
//    public ResponseEntity<Void> activateAccount(@RequestParam("data") String activationKey) {
//        userService.
//    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") String userId) {
        var usersResource = keycloakProvider.getInstance().realm(keycloakProvider.getRealm()).users().get(userId);
        var userEmail = usersResource.toRepresentation().getEmail();
//        var id = SecurityUtils.getCurrentUser();
        return ResponseEntity.ok(userMapper.mapToUserDTO(userService.findUserByEmail(userEmail)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> editUser(@PathVariable("id") String userId, @RequestBody UserDTO userDTO) {
        var usersResource = keycloakProvider.getInstance().realm(keycloakProvider.getRealm()).users().get(userId);
        var userEmail = usersResource.toRepresentation().getEmail();
        var user = userService.modifyUser(userEmail, userDTO.getFirstName(), userDTO.getLastName());
        return ResponseEntity.ok(userMapper.mapToUserDTO(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponse> login(@NotNull @RequestBody AuthRequestModel authRequestModel) {
        Keycloak keycloak = keycloakProvider.newKeycloakBuilderWithPasswordCredentials(authRequestModel.getUsername(), authRequestModel.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        TokenResponse tokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            tokenResponse = new TokenResponse(accessTokenResponse.getToken());
            return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
        } catch (BadRequestException ex) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(tokenResponse);
        }
    }

//    @PatchMapping("/{id}/change-password")
//    public ResponseEntity<Void> changeUserPassword(@PathVariable("id") String userId,
//                                                   @Valid @RequestBody ChangePassRequest request,
//                                                   Authentication authentication) {
//        if (!userId.equals(SecurityUtils.getCurrentUser())) {
//            throw new InvalidDataException("Invalid user id");
//        }
//        userService.changeUserPassword(SecurityUtils.getCurrentUser(), request.getCurrentPassword(), request.getNewPassword());
//        return ResponseEntity.noContent().build();
//    }

}
