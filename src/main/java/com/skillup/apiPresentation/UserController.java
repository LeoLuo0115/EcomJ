package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.in.UserPin;
import com.skillup.apiPresentation.util.SkillResponseUtil;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserDomainService;
import com.skillup.apiPresentation.dto.in.UserInDto;
import com.skillup.apiPresentation.dto.out.UserOutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
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

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> getUserById(@PathVariable("id") String userId){
        UserDomain userDomain = userDomainService.getUserById(userId);
        if(Objects.isNull(userDomain)){
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_ID_WRONG, userId)).build());
        }

        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillUpResponse> getUserByName(@PathVariable("name") String name){
        UserDomain userDomain = userDomainService.getUserByName(name);
        if(Objects.isNull(userDomain)){
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_NAME_WRONG, name)).build());
        }

        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());

    }

    @PostMapping("/login")
    public ResponseEntity<SkillUpResponse> login(@RequestBody UserInDto userInDto){
        UserDomain userDomain = userDomainService.getUserByName(userInDto.getUserName());
        if(Objects.isNull(userDomain)){
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_NAME_WRONG, userInDto.getUserName())).build());
        }

        if(!userInDto.getPassword().equals(userDomain.getPassword())){
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.PASSWORD_NOT_MATCH).build());
        }
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());

    }

    @PutMapping("/password")
    public ResponseEntity<SkillUpResponse> updatePassword(@RequestBody UserPin userPin){
        UserDomain userDomain = userDomainService.getUserByName(userPin.getUserName());
        if(Objects.isNull(userDomain)){
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_NAME_WRONG, userPin.getUserName())).build());
        }
        if(!userPin.getOldPassword().equals(userDomain.getPassword())){
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.PASSWORD_NOT_MATCH).build());
        }

        userDomain.setPassword(userPin.getNewPassword());
        UserDomain updateUserDomain = userDomainService.updateUser(userDomain);

        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(updateUserDomain).build());

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

