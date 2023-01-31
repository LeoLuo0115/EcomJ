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
}
