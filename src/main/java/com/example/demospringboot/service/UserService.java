package com.example.demospringboot.service;

import com.example.demospringboot.entity.User;

/**
 * Interface for User Management
 */
public interface UserService {

    /**
     * Method to register as a new user
     *
     * @param user
     * @return User
     */
    User addUser(User user);

    /**
     * Method to update the details for an existing user
     *
     * @param user
     * @return User
     */
    User updateUser(User user);
}
