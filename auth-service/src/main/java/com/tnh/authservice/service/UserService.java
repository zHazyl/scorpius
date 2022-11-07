package com.tnh.authservice.service;

import com.tnh.authservice.domain.User;

public interface UserService {
    User createUser(String username, String password, String email, String firstName, String lastName);
    User createUser(User user);
    User findUserById(String userId);

    User modifyUser(String userId, String firstName, String lastName);

}
