package com.tnh.chatmessagesservice.utils.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityUserDetails extends UserDetails {

    String getId();

}
