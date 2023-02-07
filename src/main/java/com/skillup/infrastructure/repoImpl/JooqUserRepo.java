package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.UserDomain;
import com.skillup.domain.UserRepository;
import com.skillup.infrastructure.jooq.tables.User;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.TableRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // create a Singleton and place in Spring container
public class JooqUserRepo implements UserRepository {
    @Autowired // create a DSLContext bean in Spring container
    DSLContext dslContext; // create a thread pool to connect db (provided by JOOQ)

    // generate the USER table only once
    public static final User USER_T = new User();

    @Override
    public void createUser(UserDomain userDomain) {
        dslContext.executeInsert(toRecord(userDomain));
    }

    @Override
    public void updateUser(UserDomain userDomain) {
        dslContext.executeUpdate(toRecord(userDomain));
    }

    @Override
    public UserDomain getUserById(String id) {
        Optional<UserDomain> userDomainOptional = dslContext
                .selectFrom(USER_T).where(USER_T.USER_ID.eq(id)) // this line get a record (may be null)
                .fetchOptional(this::toDomain);
        return userDomainOptional.orElse(null);
    }


    @Override
    public UserDomain getUserByName(String name) {
        Optional<UserDomain> userDomainOptional = dslContext
                .selectFrom(USER_T).where(USER_T.USER_NAME.eq(name))
                .fetchOptional(this::toDomain);
        return userDomainOptional.orElse(null);
    }

    // put assistant methods in the bottom
    private UserRecord toRecord(UserDomain userDomain) {
//        // DEBUG
//        System.out.println(userDomain.getUserId());
//        System.out.println(userDomain.getPassword());
//        System.out.println(userDomain.getUserName());

        // UserRecord is created by JOOQ so it doesn't have builder pattern
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userDomain.getUserId());
        userRecord.setUserName(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());
        return userRecord;
    }

    private UserDomain toDomain(UserRecord userRecord) {
        return UserDomain.builder()
                .userId(userRecord.getUserId())
                .userName(userRecord.getUserName())
                .password(userRecord.getPassword())
                .build();
    }

}
