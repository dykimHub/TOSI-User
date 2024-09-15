package com.tosi.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginDto {

    private String email;
    private String nickname;
    private String password;
    private boolean autoLogin;

    public boolean getAutoLogin() {
        return autoLogin;
    }
}
