package com.skillup.presentation;

import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserDomainService;
import com.skillup.presentation.dto.in.UserInDto;
import com.skillup.presentation.dto.in.UserPin;
import com.skillup.presentation.dto.out.UserOutDto;
import com.skillup.presentation.util.ResponseCode;
import com.skillup.presentation.util.SkillUpResponse;
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

    @PostMapping()
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto) {
        // create userDomain and call user domain service to create the user
        UserDomain savedUserDomain = null;
        try {
            savedUserDomain = userDomainService.registry(toDomain(userInDto));
        } catch (Exception e) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(SkillUpResponse.builder()
                    .msg(ResponseCode.USER_EXIST)
                    .build());
        }

        // create outDTO
        return ResponseEntity.status(ResponseCode.SUCCESS).body(SkillUpResponse.builder()
                .result(toOutDto(savedUserDomain))
                .build());

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> getUserById(@PathVariable("id") String userId) {
        UserDomain userDomain = userDomainService.getUserById(userId);

        // user has not found by id.
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(SkillUpResponse.builder()
                    .msg(String.format(ResponseCode.USER_ID_WRONG, userId))
                    .build());
        }
        // user found
        return ResponseEntity.status(ResponseCode.SUCCESS).body(SkillUpResponse.builder()
                .result(toOutDto(userDomain))
                .build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillUpResponse> getUserByName(@PathVariable("name") String userName) {
        UserDomain userDomain = userDomainService.getUserByName(userName);

        // user has not found by name
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(SkillUpResponse.builder()
                    .msg(String.format(ResponseCode.USER_NAME_WRONG, userName))
                    .build());
        }
        return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(SkillUpResponse.builder()
                .result(toOutDto(userDomain))
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<SkillUpResponse> login(@RequestBody UserInDto userInDto) {
        // check user by username
        UserDomain userDomain = userDomainService.getUserByName(userInDto.getUserName());

        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(SkillUpResponse.builder()
                    .msg(String.format(ResponseCode.USER_NAME_WRONG, userInDto.getUserName()))
                    .build());
        }

        // password match
        if (!userInDto.getPassword().equals(userDomain.getPassword())) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(SkillUpResponse.builder()
                    .msg(ResponseCode.PASSWORD_NOT_MATCH)
                    .build());
        }

        // match return
        return ResponseEntity.status(ResponseCode.SUCCESS).body(SkillUpResponse.builder()
                .result(toOutDto(userDomain))
                .build());
    }

    @PutMapping("/password")
    public ResponseEntity<SkillUpResponse> updatePassword(@RequestBody UserPin userPin) {
        // get user
        UserDomain userDomain = userDomainService.getUserByName(userPin.getUserName());

        // user exist
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(SkillUpResponse.builder()
                    .msg(String.format(ResponseCode.USER_NAME_WRONG, userPin.getUserName()))
                    .build());
        }
        // check password
        if (!userPin.getOldPassword().equals(userDomain.getPassword())) {
            return ResponseEntity.status(ResponseCode.BAD_REQUEST).body(SkillUpResponse.builder()
                    .msg(ResponseCode.PASSWORD_NOT_MATCH)
                    .build());
        }

        // update password
        userDomain.setPassword(userPin.getNewPassword());
        UserDomain updatedUserDomain = userDomainService.updateUserPassword(userDomain);
        return ResponseEntity.status(ResponseCode.SUCCESS).body(SkillUpResponse.builder()
                .result(toOutDto(updatedUserDomain))
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
