package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.UserDomain;
import com.skillup.domain.UserRepository;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.TableRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository // create a Singleton and place in Spring container
public class JooqUserRepo implements UserRepository {
    @Autowired // create a DSLContext bean in Spring container
    DSLContext dslContext;
    @Override
    public void createUser(UserDomain userDomain) {

        dslContext.executeInsert(toRecord(userDomain));
    }

    @Override
    public void updateUser(UserDomain userDomain) {

    }

    @Override
    public UserDomain getUserById(String id) {
        return null;
    }

    @Override
    public UserDomain getUserByName(String name) {
        return null;
    }

    // put assistant methods in the bottom
    private UserRecord toRecord(UserDomain userDomain) {
        // DEBUG
        System.out.println(userDomain.getUserId());
        System.out.println(userDomain.getPassword());
        System.out.println(userDomain.getUserName());

        // UserRecord is created by JOOQ so it doesn't have builder pattern
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userDomain.getUserId());
        userRecord.setUserName(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());
        return userRecord;
    }

}
