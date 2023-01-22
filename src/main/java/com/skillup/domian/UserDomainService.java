package com.skillup.domian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {

    @Autowired
    UserRepository userRepository;

    public UserDomain registry(UserDomain userDomain) {
        // call ORM tool JOOQ to create data into database
        userRepository.createUser(userDomain);
        return userDomain;
    }
}
