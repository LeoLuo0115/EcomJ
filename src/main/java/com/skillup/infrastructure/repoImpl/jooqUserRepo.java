package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserRepository;
import com.skillup.infrastructure.jooq.tables.User;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class jooqUserRepo implements UserRepository {

    @Autowired
    DSLContext dslContext;

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
    public UserDomain getUserById(String userId) {
        Optional<UserDomain> userDomainOptional = dslContext
                .selectFrom(USER_T)
                .where(USER_T.USER_ID.eq(userId))
                .fetchOptional(this::toDomain);

        return userDomainOptional.orElse(null);

    }

    @Override
    public UserDomain getUserByName(String userName)  {
        Optional<UserDomain> userDomainOptional = dslContext
                .selectFrom(USER_T)
                .where(USER_T.USER_NAME.eq(userName))
                .fetchOptional(this::toDomain);

        return userDomainOptional.orElse(null);

    }

    private UserDomain toDomain(UserRecord userRecord) {
        return UserDomain.builder()
                .userName(userRecord.getUserName())
                .userId(userRecord.getUserId())
                .password(userRecord.getPassword())
                .build();
    }

    private UserRecord toRecord(UserDomain userDomain) {
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userDomain.getUserId());
        userRecord.setUserName(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());
        return userRecord;
    }
}
