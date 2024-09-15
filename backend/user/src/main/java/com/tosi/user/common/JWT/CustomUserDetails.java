package com.tosi.user.common.JWT;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@JsonIgnoreProperties(value = {"enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetails implements UserDetails {
    /**
     * UserDetails 인터페이스를 구현한 CustomUserDetails 클래스
     * 이 클래스는 UserDetails 인터페이스에 정의된 username, password, authorities, enabled, accountNonExpired, accountNonLocked, credentialsNonExpired 필드를 사용
     * enabled, accountNonExpired, accountNonLocked, credentialsNonExpired 필드는 잠김 계정 등을 표시하는 필드로, 현재 프로젝트에서는 필요하지 않아 JsonIgnore로 처리하여 직렬화에서 제외
     * 레디스에서 List<GrantedAuthority>의 역직렬화 문제로 인해 authorities 필드는 빈 리스트로 초기화하며, 생성자에서는 이 필드를 다루지 않음
     */

    private final List<GrantedAuthority> authorities = new ArrayList<>();
    private String username;
    private String password;

    @Builder
    public CustomUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
