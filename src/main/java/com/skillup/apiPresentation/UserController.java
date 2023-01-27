package com.skillup.apiPresentation;

import com.skillup.apiPresentation.util.SkillResponseUtil;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.domain.UserDomain;
import com.skillup.domain.UserDomainService;
import com.skillup.apiPresentation.dto.in.UserInDto;
import com.skillup.apiPresentation.dto.out.UserOutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/account") // requestMapping for common part of url
public class UserController {

    @Autowired
    UserDomainService userDomainService;

    @PostMapping
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto){

        UserDomain userDomain = toDomain(userInDto);

        UserDomain savedUserDomain = null;
        try {
            savedUserDomain = userDomainService.registry(userDomain);
        } catch (Exception e) {

            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.USER_EXISTS).build());
        }
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(savedUserDomain)).build());


    }

    private UserDomain toDomain(UserInDto userInDto){
        return UserDomain.builder()
                .userId(UUID.randomUUID().toString())
                .userName(userInDto.getUserName())
                .password(userInDto.getPassword())
                .build();
    }

    private UserOutDto toOutDto(UserDomain userDomain){
        return UserOutDto.builder()
                .userId(userDomain.getUserId())
                .userName(userDomain.getUserName())
                .build();
    }
}

