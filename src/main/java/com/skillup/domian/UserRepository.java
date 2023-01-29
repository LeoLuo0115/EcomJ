package com.skillup.domian;

import com.skillup.infrastructure.jooq.tables.User;

public interface UserRepository {
    void createUser(UserDomain userDomain);

    void updateUser(UserDomain userDomain);

    UserDomain getUserByID(String id);

    UserDomain getUserByName(String name);

}
