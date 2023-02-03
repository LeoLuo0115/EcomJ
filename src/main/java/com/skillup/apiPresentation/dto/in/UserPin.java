package com.skillup.apiPresentation.dto.in;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPin {

    private String userName;

    private String oldPassword;

    private String newPassword;

}
