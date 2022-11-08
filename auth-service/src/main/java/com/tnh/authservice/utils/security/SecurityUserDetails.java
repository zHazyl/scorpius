package com.tnh.authservice.utils.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityUserDetails extends UserDetails {

    String getId();

}
