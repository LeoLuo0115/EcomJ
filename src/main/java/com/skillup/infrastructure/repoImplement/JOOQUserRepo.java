package com.skillup.infrastructure.repoImplement;

import com.skillup.domian.UserDomain;
import com.skillup.domian.UserRepository;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JOOQUserRepo implements UserRepository {

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
    public UserDomain getUserByID(String id) {
        return null;
    }

    @Override
    public UserDomain getUserByName(String name) {
        return null;
    }

    private UserRecord toRecord(UserDomain userDomain) {
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userDomain.getUserID());
        userRecord.setUserName(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());

        return userRecord;
    }
}
