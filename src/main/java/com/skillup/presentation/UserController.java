package com.skillup.presentation;

import com.skillup.domain.UserDomain;
import com.skillup.domain.UserDomainService;
import com.skillup.presentation.dto.in.UserInDto;
import com.skillup.presentation.dto.out.UserOutDto;
import com.skillup.presentation.util.SkillUpResponse;
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

    @PostMapping()
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto) {
        // create userDomain and call user domain service to create the user
        UserDomain savedUserDomain = null;
        try {
            savedUserDomain = userDomainService.registry(toDomain(userInDto));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(SkillUpResponse.builder()
                    .msg("User already exists")
                    .build());
        }

        // create outDTO
        return ResponseEntity.status(200).body( SkillUpResponse.builder()
                .response(savedUserDomain)
                .build());

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
