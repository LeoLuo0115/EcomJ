package com.skillup.infrastructure.repoImplement;

import com.skillup.domian.user.UserDomain;
import com.skillup.domian.user.UserRepository;
import com.skillup.infrastructure.jooq.tables.User;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JOOQUserRepo implements UserRepository {

    @Autowired
    DSLContext dslContext;

    public static final User User_T = new User();

    @Override
    public void createUser(UserDomain userDomain) {
        dslContext.executeInsert(toRecord(userDomain));

    }


    @Override
    public void updateUser(UserDomain userDomain) {
        dslContext.executeUpdate(toRecord(userDomain));
    }



    @Override
    public UserDomain getUserByID(String id) {
        Optional<UserDomain> userDomainOptional =  dslContext.selectFrom(User_T).where(User_T.USER_ID.eq(id)).fetchOptional(this::toDomain);
        return userDomainOptional.orElse(null);
    }

    @Override
    public UserDomain getUserByName(String name) {
        Optional<UserDomain> userDomainOptional =  dslContext.selectFrom(User_T).where(User_T.USER_NAME.eq(name)).fetchOptional(this::toDomain);
        return userDomainOptional.orElse(null);
    }

    private UserRecord toRecord(UserDomain userDomain) {
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId(userDomain.getUserID());
        userRecord.setUserName(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());

        return userRecord;
    }

    private UserDomain toDomain(UserRecord userRecord) {
        return UserDomain.builder()
                .userID(userRecord.getUserId())
                .userName(userRecord.getUserName())
                .password(userRecord.getPassword())
                .build();
    }
}
