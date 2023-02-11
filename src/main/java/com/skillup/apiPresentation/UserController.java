package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.in.UserPin;
import com.skillup.apiPresentation.util.SkillUpResponseUtil;
import com.skillup.domain.UserDomain;
import com.skillup.domain.UserDomainService;
import com.skillup.apiPresentation.dto.in.UserInDto;
import com.skillup.apiPresentation.dto.out.UserOutDto;
import com.skillup.apiPresentation.util.SkillUpResponse;
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

    @PostMapping// route inherit from the outer class
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto){
        // Domain layer only deals with Domain, so we need to convert Dto to domain, then to outDto
        UserDomain savedUserDomain = null;
        try {
            // create userDomain
            UserDomain userDomain = toDomain(userInDto);
            // call domain service to register user
            savedUserDomain = userDomainService.registry(userDomain);
        }catch(Exception e){
            // provide error message, set result equals to null
            return  ResponseEntity.status(SkillUpResponseUtil.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                    .msg(SkillUpResponseUtil.USER_EXISTS)
                    .build());
        }

        // create outDto and encapsulate it into a Response object
        // return toOutDto(savedUserDomain);
        // don't hard code http status code, refer to the util package and change output centrally
        return ResponseEntity.status(SkillUpResponseUtil.SUCCESS).body(
                SkillUpResponse.builder()
                .result(toOutDto(savedUserDomain))
                .build());
    }

    @GetMapping("/id/{id}") // route inherit from the outer class
    public ResponseEntity<SkillUpResponse> getUserById(@PathVariable("id") String userId){
        // user service get user
        UserDomain userDomain = userDomainService.getUserById(userId);
        // user not exist
        if (Objects.isNull(userDomain)) {
            return  ResponseEntity.status(SkillUpResponseUtil.BAD_REQUEST).body(
                            SkillUpResponse.builder()
                            .msg(String.format(SkillUpResponseUtil.USER_ID_WRONG, userId))
                            .build());
        }
        return ResponseEntity.status(SkillUpResponseUtil.SUCCESS).body(
                SkillUpResponse.builder().result(toOutDto(userDomain)).build()
        );
    }

    @GetMapping("/name/{name}") // route inherit from the outer class
    public ResponseEntity<SkillUpResponse> getUserByName(@PathVariable("name") String name){
        // user service get user
        UserDomain userDomain = userDomainService.getUserByName(name);
        // user not exist
        if (Objects.isNull(userDomain)) {
            return  ResponseEntity.status(SkillUpResponseUtil.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(String.format(SkillUpResponseUtil.USER_NAME_WRONG, name))
                            .build());
        }
        return ResponseEntity.status(SkillUpResponseUtil.SUCCESS).body(
                SkillUpResponse.builder().result(toOutDto(userDomain)).build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<SkillUpResponse> login(@RequestBody UserInDto userInDto){
        // try to get user
        UserDomain userDomain = userDomainService.getUserByName(userInDto.getUserName());
        // check user existence
        if (Objects.isNull(userDomain)) {
            return  ResponseEntity.status(SkillUpResponseUtil.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(String.format(SkillUpResponseUtil.USER_NAME_WRONG, userInDto.getUserName()))
                            .build());
        }
        // password unmatch
        if(!userInDto.getPassword().equals(userDomain.getPassword())){
            return  ResponseEntity.status(SkillUpResponseUtil.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(SkillUpResponseUtil.PASSWORD_NOT_MATCH)
                            .build());
        }
        // match return
        return ResponseEntity.status(SkillUpResponseUtil.SUCCESS).body(
                SkillUpResponse.builder().result(toOutDto(userDomain)).build()
        );
    }

    @PutMapping("/password")
    public ResponseEntity<SkillUpResponse> updatePassword(@RequestBody UserPin userPin){
        // get user
        UserDomain userDomain = userDomainService.getUserByName(userPin.getUserName());
        // verify user exist
        if (Objects.isNull(userDomain)) {
            return  ResponseEntity.status(SkillUpResponseUtil.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(String.format(SkillUpResponseUtil.USER_NAME_WRONG, userPin.getUserName()))
                            .build());
        }
        // check old password - password unmatch
        if(!userPin.getOldPassword().equals(userDomain.getPassword())){
            return  ResponseEntity.status(SkillUpResponseUtil.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(SkillUpResponseUtil.PASSWORD_NOT_MATCH)
                            .build());
        }
        // update password
        userDomain.setPassword(userPin.getNewPassword()); // this line can be moved to UserDomainService
        UserDomain updateUserDomain = userDomainService.updateUser(userDomain);
        return  ResponseEntity.status(SkillUpResponseUtil.SUCCESS).body(
                SkillUpResponse.builder()
                        .result(toOutDto(updateUserDomain)).build()
        );
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
