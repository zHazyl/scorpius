package com.tnh.authservice.service.impl;

import com.tnh.authservice.config.KeycloakProvider;
import com.tnh.authservice.dto.UserDTO;
import com.tnh.authservice.service.KeycloakAdminClientService;
import com.tnh.authservice.utils.UserContextFilter;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
@Slf4j
public class KeycloakAdminClientServiceImpl implements KeycloakAdminClientService {

    //private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminClientServiceImpl.class);


    @Value("${keycloak.realm}")
    public String realm;

    private final KeycloakProvider keycloakProvider;

    public KeycloakAdminClientServiceImpl(KeycloakProvider keycloakProvider) {
        this.keycloakProvider = keycloakProvider;
    }

    @Override
    public Response createKeycloakUser(UserDTO userDTO) {
        UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userDTO.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
//        kcUser.setId(userDTO.getId().toString());
        kcUser.setUsername(userDTO.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
//        kcUser.setFirstName(userDTO.getFirstName());
//        kcUser.setLastName(userDTO.getLastName());
        kcUser.setEmail(userDTO.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            // if you want to save the user to your other database, do it here:
            // I already have saved before
        }

        return response;

    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}
