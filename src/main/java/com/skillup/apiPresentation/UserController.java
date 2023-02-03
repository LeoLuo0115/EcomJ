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

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> getUserById(@PathVariable("id") String userId){
        //user service get user
        UserDomain userDomain = userDomainService.getUserById(userId);
        //user not exist
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_ID_WRONG)).build());
        }
        //user exists
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillUpResponse> getUserByName(@PathVariable("name") String name) {
        //user Service get user
        UserDomain userDomain = userDomainService.getUserByName(name);
        //user not exists
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_NAME_WRONG)).build());
        }
        //user exists
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }

    @PostMapping("/login")
    public ResponseEntity<SkillUpResponse> login(@RequestBody UserInDto userInDto){
        //1 try to get user by name
        UserDomain userDomain = userDomainService.getUserByName(userInDto.getUserName());
        // 2 user exists?
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_NAME_WRONG)).build());
        }
        // 3 password match
        if (!userInDto.getPassword().equals(userDomain.getPassword())) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.PASSWORD_NOT_MATCH).build());
        }
        // 4 match return
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }

    @PutMapping("/password")
    public ResponseEntity<SkillUpResponse> updatePassword(@RequestBody UserPin userPin) {
        // 1 get user
        UserDomain userDomain = userDomainService.getUserByName(userPin.getUserName());
        // 2 user exist
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(SkillResponseUtil.USER_EXISTS)).build());
        }
        // 3 check old password
        if (!userPin.getOldPassword().equals(userDomain.getPassword())) {
            return ResponseEntity.status(SkillResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(SkillResponseUtil.PASSWORD_NOT_MATCH).build());
        }

        // 4 update password
        userDomain.setPassword(userPin.getNewPassword());
        UserDomain updateUserDomain =  userDomainService.updateUser(userDomain);
        return ResponseEntity.status(SkillResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(updateUserDomain)).build());

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
