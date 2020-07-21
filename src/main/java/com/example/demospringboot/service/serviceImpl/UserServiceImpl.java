package com.example.demospringboot.service.serviceImpl;

import com.example.demospringboot.entity.User;
import com.example.demospringboot.exceptions.ResourceNotFoundException;
import com.example.demospringboot.repo.UserRepo;
import com.example.demospringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Implementation for User Service
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User addUser(User user) {
        if (user.getUserName() != null) {
            return userRepo.save(user);
        } else {
            throw new ResourceNotFoundException("Cannot add user");
        }
    }

    @Override
    public User updateUser(User user) {

        Optional<User> userOptionalData = userRepo.findById(user.getUserId());
        if (userOptionalData.isPresent()) {
            User userData = userOptionalData.get();
            userData.setUserName(user.getUserName());
            userData.setMobileNo(user.getMobileNo());
            userData.setEmailId(user.getEmailId());
            userData.setAddress1(user.getAddress1());
            userData.setAddress2(user.getAddress2());

            return userRepo.save(userData);
        } else {
            throw new ResourceNotFoundException("User not found for id : " + user.getUserId());
        }
    }
}
