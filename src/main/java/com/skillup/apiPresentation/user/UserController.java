package com.skillup.apiPresentation.user;

import com.skillup.domain.UserDomain;
import com.skillup.domain.UserDomainService;
import com.skillup.apiPresentation.user.dto.in.UserInDto;
import com.skillup.apiPresentation.user.dto.out.UserOutDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/account")
public class UserController {
    UserDomainService userDomainService = new UserDomainService();
    @PostMapping
    public UserOutDto createUser(@RequestBody UserInDto userInDto){
        //create userDomain
        UserDomain userDomain = toDomain(userInDto);
        //call domain service to create user
        UserDomain savedUserDomainService = userDomainService.registry(userDomain);
        //create OutDto
        return toOutDto(savedUserDomainService);
    }
    private UserDomain toDomain(UserInDto userInDto){
        UserDomain userDomain = new UserDomain();
        userDomain.setUserId(UUID.randomUUID().toString());
        userDomain.setUserName(userInDto.getUserName());
        userDomain.setPassword(userInDto.getPassword());

        return userDomain;
    }

    private UserOutDto toOutDto(UserDomain userDomain){
        UserOutDto userOutDto = new UserOutDto();
        userOutDto.setUserId(userOutDto.getUserId());
        userOutDto.setUserName(userOutDto.getUserName());
        return userOutDto;
    }
}
