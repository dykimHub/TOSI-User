package com.tosi.user.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginInfo {

    private String email;
    private String password;
    private boolean autoLogin;

    public boolean getAutoLogin() {
        return autoLogin;
    }
}
