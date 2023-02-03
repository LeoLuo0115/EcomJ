package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.UserDomain;
import com.skillup.domain.UserRepository;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.TableRecord;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class jooqUserRepo implements UserRepository {

    @Autowired
    DSLContext dslContext;
    @Override
    public void createUser(UserDomain userDomain) {
        dslContext.executeInsert(toRecord(userDomain));
    }

    @Override
    public void updateUser(UserDomain userDomain) {

    }

    @Override
    public UserDomain getUserById(String userId) {
        return null;
    }

    @Override
    public UserDomain getUserByName(String userName) {
        return null;
    }

    private UserRecord toRecord(UserDomain userDomain) {
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userDomain.getUserId());
        userRecord.setUserName(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());
        return userRecord;
    }
}
