package com.skillup.domain.user;

public interface UserRepository {

    void createUser(UserDomain userDomain);

    void updateUser(UserDomain userDomain);

    UserDomain getUserById(String id);

    UserDomain getUserByName(String name);
}
