package com.tnh.messageswebsocketservice.utils.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityUserDetails extends UserDetails {

    String getId();

}
