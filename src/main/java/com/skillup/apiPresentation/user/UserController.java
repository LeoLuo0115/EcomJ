package com.skillup.apiPresentation.user;

import com.skillup.domain.UserDomain;
import com.skillup.domain.UserDomainService;
import com.skillup.apiPresentation.user.dto.in.UserInDto;
import com.skillup.apiPresentation.user.dto.out.UserOutDto;
import com.skillup.apiPresentation.util.SkillUpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/account")
public class UserController {
    @Autowired
    UserDomainService userDomainService;
    @RequestMapping() // route inherit from the outer class
    public SkillUpResponse createUser(@RequestBody UserInDto userInDto){
        // Domain layer only deals with Domain, so we need to convert Dto to domain, then to outDto
        // create userDomain
        UserDomain userDomain = toDomain(userInDto);
        // call domain service to register user
        UserDomain savedUserDomain = userDomainService.registry(userDomain);
        // create outDto and encapsulate it into a Response object
//        return toOutDto(savedUserDomain);
        return SkillUpResponse.builder()
                .result(toOutDto(savedUserDomain))
                .build();
    }
    private UserDomain toDomain(UserInDto userInDto){
        return UserDomain.builder() // return a Builder class (no "new" explicitly)
                .userId(UUID.randomUUID().toString()) // return that Builder class
                .userName(userInDto.getUserName()) // return that Builder class
                .password(userInDto.getPassword()) // return that Builder class
                .build(); // Builder class gets everything, create a UserDomain class
    }

    private UserOutDto toOutDto(UserDomain userDomain){
        return UserOutDto.builder()
                .userId(userDomain.getUserId())
                .userName(userDomain.getUserName())
                .build();
    }
}
