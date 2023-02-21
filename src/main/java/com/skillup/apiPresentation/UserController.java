package com.skillup.apiPresentation;

import com.skillup.apiPresentation.dto.in.UserInDto;
import com.skillup.apiPresentation.dto.in.UserInPin;
import com.skillup.apiPresentation.dto.out.UserOutDto;
import com.skillup.apiPresentation.util.ResponseUtil;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.domian.user.UserDomain;
import com.skillup.domian.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class UserController {

    // 用 new 不好的原因是因为每有一个请求来，都要new一个对象，但是对象功能是简单的，并且公共的，只是往数据库里存取数据
    // UserDomainService userDomainService = new UserDomainService();

    // 单例模式
    // public static UserDomainService userDomainService = new UserDomainService();

    // 我们要把 Service 做成单例模式，要在 Service上加上 @Service, 不需要自己再 new 单例，Spring 会帮我们 new 一个单例出来
    // 并且把 单例存到一个 map里, 使用关键词 @Autowired 来使用存放的 单例

    @Autowired
    UserService userService;

    // API
    @PostMapping
    public ResponseEntity<SkillUpResponse> creatUser(@RequestBody UserInDto userInDto) {
        // create userDomain
        // UserDomain userDomain = toDomain(userInDto);

        UserDomain saveduserDomain = null;

        try {
            // call domain service to create user
            saveduserDomain = userService.registry(toDomain(userInDto));
        } catch (Exception e) {
            // provide error message, set result equals null
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(ResponseUtil.USER_EXISTS).build());

        }

        //create OutDto
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(saveduserDomain)).build());

    }


    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> getUserByID(@PathVariable("id") String userID) {
        UserDomain userDomain = userService.getUserByID(userID);

        // user not exist
        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(ResponseUtil.USER_ID_WRONG, userID)).build());
        }
        // user exists
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<SkillUpResponse> getUserByName(@PathVariable("name") String name) {
        UserDomain userDomain = userService.getUserByName(name);

        if (Objects.isNull(userDomain)) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(ResponseUtil.USER_NAME_WRONG, name)).build());
        }

        return ResponseEntity.status(ResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }


    @PostMapping("/login")
    public ResponseEntity<SkillUpResponse> login(@RequestBody UserInDto userInDto) {
        // 1. get user by name
        UserDomain userDomain = userService.getUserByName(userInDto.getUserName());

        // 2. check user exists
        if(Objects.isNull(userDomain)) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(ResponseUtil.USER_NAME_WRONG, userInDto.getUserName())).build());
        }
        // 3. password not match
        if(!userInDto.getPassword().equals(userDomain.getPassword())) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(ResponseUtil.PASSWORD_NOT_MATCH)).build());
        }

        // 4. match return
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(userDomain)).build());
    }


    @PutMapping("/password")
    public ResponseEntity<SkillUpResponse> updatePassword(@RequestBody UserInPin userInPin) {
        // 1. get user
        UserDomain userDomain = userService.getUserByName(userInPin.getUserName());

        // 2. user not exists
        if(Objects.isNull(userDomain)) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(ResponseUtil.USER_NAME_WRONG, userInPin.getUserName())).build());
        }
        // 3. check old password
        if (!userInPin.getOldPassword().equals(userDomain.getPassword())) {
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(String.format(ResponseUtil.PASSWORD_NOT_MATCH)).build());
        }

        // 4. update new pin
        userDomain.setPassword(userInPin.getNewPassword());
        UserDomain updateedUserDomain = userService.updateUser(userDomain);
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(updateedUserDomain)).build());
    }




    // Data type transfer
    // JAVA Builder 模式， Simple Method Chain
    private UserDomain toDomain(UserInDto userInDto) {
        return UserDomain.builder()
                .userID(UUID.randomUUID().toString())
                .userName(userInDto.getUserName())
                .password(userInDto.getPassword())
                .build();
    }


    private UserOutDto toOutDto(UserDomain userDomain) {
        return UserOutDto.builder()
                .userId(userDomain.getUserID())
                .userName(userDomain.getUserName())
                .build();
    }

}
