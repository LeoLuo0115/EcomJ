package com.skillup.APIpresentation;

import com.skillup.APIpresentation.dto.in.UserInDto;
import com.skillup.APIpresentation.dto.out.UserOutDto;
import com.skillup.APIpresentation.util.SkillUpResponse;
import com.skillup.domian.UserDomain;
import com.skillup.domian.UserDomainService;
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
    public SkillUpResponse creatUser(@RequestBody UserInDto userInDto) {
        // create userDomain
        UserDomain userDomain = toDomain(userInDto);
        UserDomain saveeduserDomain = userDomainService.registry(userDomain);


    }

    // JAVA Builder 模式， Simple Method Chain
    private UserDomain toDomain(UserInDto userInDto) {
        return new UserDomain()
                .userID(UUID.randomUUID().toString())
                .userName(userInDto.getUserName())
                .password(userInDto.getPassword());
    }


    private UserOutDto toOutDto(UserDomain userDomain) {
        UserOutDto userOutDto = new UserOutDto();
        userOutDto.setUserId(userDomain.getUserID());
        userOutDto.setUserName(userDomain.getUserName());

        return userOutDto;
    }

}
