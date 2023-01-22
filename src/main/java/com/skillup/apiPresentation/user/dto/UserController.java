package com.skillup.apiPresentation.user.dto;

import com.skillup.apiPresentation.user.util.SkillResponseUtil;
import com.skillup.apiPresentation.user.util.SkillUpResponse;
import com.skillup.domain.UserDomain;
import com.skillup.domain.UserDomainService;
import com.skillup.apiPresentation.user.dto.in.UserInDto;
import com.skillup.apiPresentation.user.dto.out.UserOutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/account")
public class UserController {

    @Autowired
    UserDomainService userDomainService;

    @PostMapping
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto){
        //crete userDomain
        UserDomain savedUserDomain = null;
       //call domain service to create user
        try{
            savedUserDomain = userDomainService.registry(toDomain(userInDto));
        }catch (Exception e){
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.USER_EXISTS).build());
        }

        // create outDto
         return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(savedUserDomain)).build());
    }

    private UserDomain toDomain(UserInDto userInDto) {

        return UserDomain.builder()
                .userId(UUID.randomUUID().toString())
                .userName(userInDto.getUserName())
                .password(userInDto.getPassword())
                .build();
    }

    private UserOutDto toOutDto(UserDomain userDomain) {
        return UserOutDto.builder()
                .userId(userDomain.getUserId())
                .userName(userDomain.getUserName())
                .build();
    }
}
