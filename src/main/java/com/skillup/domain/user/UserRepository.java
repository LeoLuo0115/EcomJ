package com.skillup.domain.user;

public interface UserRepository {
    void createUser(UserDomain userDomain);
    void updateUser(UserDomain userDomain);
    UserDomain getUserById(String userId);
    UserDomain getUserByName(String userName);
}
