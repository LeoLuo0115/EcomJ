package com.skillup.apiPresentation;

import com.skillup.apiPresentation.util.SkillResponseUtil;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.domain.UserDomain;
import com.skillup.domain.UserDomainService;
import com.skillup.apiPresentation.dto.in.UserInDto;
import com.skillup.apiPresentation.dto.out.UserOutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class UserController {

    @Autowired
    UserDomainService userDomainService;

    @PostMapping
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto) {
        UserDomain savedUserDomain = null;
        try {
            // call domain service to create user
            savedUserDomain = userDomainService.registry(toDomain(userInDto));
        } catch (Exception e) {
            // provide error message, set result equals null
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.USER_EXISTS).build());
        }
        // create outDto
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(savedUserDomain)).build());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> getUserById(@PathVariable("id") String userId) {
        // user service get user
        UserDomain userDomain = userDomainService.getUserById(userId);
        // user not exist
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_ID_WRONG, userId)).build());
        }
        // user exists
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillUpResponse> getUserByName(@PathVariable("name") String name) {
        // user service get user
        UserDomain userDomain = userDomainService.getUserByName(name);
        // user not exist
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_NAME_WRONG, name)).build());
        }
        // user exists
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
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
