package com.skillup.domian.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserDomain registry(UserDomain userDomain) {
        // call ORM tool JOOQ to create data into database
        userRepository.createUser(userDomain);
        return userDomain;
    }

    public UserDomain getUserByID(String userID) {
        return userRepository.getUserByID(userID);
    }

    public UserDomain getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    public UserDomain updateUser(UserDomain userDomain) {
        userRepository.updateUser(userDomain);
        return userDomain;
    }
}
