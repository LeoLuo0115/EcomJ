package com.skillup.APIpresentation;

import com.skillup.APIpresentation.dto.in.UserInDto;
import com.skillup.APIpresentation.dto.out.UserOutDto;
import com.skillup.APIpresentation.util.ResponseUtil;
import com.skillup.APIpresentation.util.SkillUpResponse;
import com.skillup.domian.UserDomain;
import com.skillup.domian.UserDomainService;
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

    // 用 new 不好的原因是因为每有一个请求来，都要new一个对象，但是对象功能是简单的，并且公共的，只是往数据库里存取数据
    // UserDomainService userDomainService = new UserDomainService();

    // 单例模式
    // public static UserDomainService userDomainService = new UserDomainService();

    // 我们要把 Service 做成单例模式，要在 Service上加上 @Service, 不需要自己再 new 单例，Spring 会帮我们 new 一个单例出来
    // 并且把 单例存到一个 map里, 使用关键词 @Autowired 来使用存放的 单例

    @Autowired
    UserDomainService userDomainService;

    @PostMapping
    public ResponseEntity<SkillUpResponse> creatUser(@RequestBody UserInDto userInDto) {
        // create userDomain
        // UserDomain userDomain = toDomain(userInDto);

        UserDomain saveduserDomain = null;

        try {
            // call domain service to create user
            saveduserDomain = userDomainService.registry(toDomain(userInDto));
        } catch (Exception e) {
            // provide error message, set result equals null
            return ResponseEntity.status(ResponseUtil.BAD_REQUEST).body(SkillUpResponse.builder().msg(ResponseUtil.USER_EXISTS).build());

        }

        //create OutDto
        return ResponseEntity.status(ResponseUtil.SUCCESS).body(SkillUpResponse.builder().result(toOutDto(saveduserDomain)).build());

    }

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
