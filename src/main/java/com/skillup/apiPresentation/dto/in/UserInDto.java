package com.skillup.apiPresentation.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Getters and Setters
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInDto {
    private String userName;
    private String password;
}
