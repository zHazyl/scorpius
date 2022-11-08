package com.tnh.authservice.service;

import com.tnh.authservice.dto.UserDTO;

import javax.ws.rs.core.Response;

public interface KeycloakAdminClientService {

    public Response createKeycloakUser(UserDTO userDTO) ;
}
