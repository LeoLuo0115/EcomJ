package com.skillup.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {
    @Autowired
    UserRepository userRepository; // Dynamic binding by Spring
    public UserDomain registry(UserDomain userDomain){
        // call ORM to create data in the database
        userRepository.createUser(userDomain);
        return userDomain;
    };

    public UserDomain getUserById(String userId) {
        UserDomain userDomain = userRepository.getUserById(userId);
        return userDomain;
    }

    public UserDomain getUserByName(String name) {
        UserDomain userDomain = userRepository.getUserByName(name);
        return userDomain;
    }
}
