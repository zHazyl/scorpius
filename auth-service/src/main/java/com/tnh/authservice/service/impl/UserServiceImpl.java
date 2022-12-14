package com.tnh.authservice.service.impl;

import com.tnh.authservice.config.KeycloakProvider;
import com.tnh.authservice.constants.ApplicationConstants;
import com.tnh.authservice.domain.User;
import com.tnh.authservice.repository.UserRedisRepository;
import com.tnh.authservice.repository.UserRepository;
import com.tnh.authservice.service.KeycloakAdminClientService;
import com.tnh.authservice.service.UserService;
//import com.tnh.authservice.utils.SecurityUtils;
import com.tnh.authservice.utils.SecurityUtils;
import com.tnh.authservice.utils.exception.AlreadyExistsException;
import com.tnh.authservice.utils.exception.InvalidDataException;
import com.tnh.authservice.utils.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.apache.commons.lang.StringUtils.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserRedisRepository userRedisRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakProvider keycloakProvider;
    private final KeycloakAdminClientService keycloakAdminClientService;


    public UserServiceImpl(UserRepository userRepository, UserRedisRepository userRedisRepository, PasswordEncoder passwordEncoder, KeycloakProvider keycloakProvider, KeycloakAdminClientService keycloakAdminClientService) {
        this.userRepository = userRepository;
        this.userRedisRepository = userRedisRepository;
        this.passwordEncoder = passwordEncoder;
        this.keycloakProvider = keycloakProvider;
        this.keycloakAdminClientService = keycloakAdminClientService;
    }

    @Override
    public User createUser(String username, String password, String email, String firstName, String lastName) {

        if (isEmpty(username) || isBlank(username) || !StringUtils.isAlphanumeric(username)) {
            throw new InvalidDataException("Invalid username");
        }
        if (isEmpty(password) || isBlank(password)) {
            throw new InvalidDataException("Invalid password");
        }
        if (password.length() < ApplicationConstants.USER_PASSWORD_MIN_LENGTH
                || password.length() > ApplicationConstants.USER_PASSWORD_MAX_LENGTH) {
            throw new InvalidDataException("Invalid password length");
        }
        if (!new EmailValidator().isValid(email, null)) {
            throw new InvalidDataException("Invalid email");
        }
        if (isEmpty(firstName) || isBlank(firstName)) {
            throw new InvalidDataException("Invalid first name");
        }
        if (isEmpty(lastName) || isBlank(lastName)) {
            throw new InvalidDataException("Invalid last name");
        }
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new AlreadyExistsException("User with username " + username + " already exists.");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new AlreadyExistsException("User with email " + email + " already exists.");
        }

        var user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setActivationKey(RandomStringUtils.randomAlphanumeric(124));
        user.setFirstName(StringUtils.capitalize(firstName.toLowerCase()));
        user.setLastName(StringUtils.capitalize(lastName.toLowerCase()));
        user.setEmail(email);

        return userRepository.save(user);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException("Not found user with id " + userId));
    }

    @Override
    public User findUserByEmail(String userEmail) {
        User user = null;
        try {
            user = userRedisRepository.findUserByEmail(userEmail);
        } catch (Exception e) {

        }
        if (user != null) {
            return user;
        }

        try {
            user = userRepository.findByEmail(userEmail);
            userRedisRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new NotFoundException("Not found user with id " + userEmail);
        }
    }

    @Override
    public User modifyUser(String email, String firstName, String lastName) {

        var user = findUserByEmail(email);
        throwExceptionIfNotCurrentUser(user);

        if (isNotEmpty(firstName)) {
            user.setFirstName(StringUtils.capitalize(firstName.toLowerCase().replaceAll(" ", "")));
        }

        if (isNotEmpty(lastName)) {
            user.setLastName(StringUtils.capitalize(lastName.toLowerCase().replaceAll(" ", "")));
        }

        try {
            userRedisRepository.deleteUser(email);
            userRedisRepository.save(user);
        } catch (Exception e) {

        }

        return userRepository.save(user);
    }

    @Override
    public void changeUserPassword(String userId, String currentPassword, String newPassword) {
        var user = findUserById(userId);
        throwExceptionIfNotCurrentUser(user);

//        var usersResource = keycloakProvider.getInstance().realm(keycloakProvider.realm).users().get(userId);
//        usersResource.resetPassword();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidDataException("Incorrect current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void throwExceptionIfNotCurrentUser(User user) {
        if (!user.getUsername().toString().equals(SecurityUtils.getCurrentUserPreferredUsername(keycloakProvider))) {
            throw new InvalidDataException("Incorrect user id");
        }
    }
}
