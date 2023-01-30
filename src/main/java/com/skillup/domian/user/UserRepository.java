package com.skillup.domian.user;

public interface UserRepository {
    void createUser(UserDomain userDomain);

    void updateUser(UserDomain userDomain);

    UserDomain getUserByID(String id);

    UserDomain getUserByName(String name);

}
