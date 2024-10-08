package com.tosi.user.common.JWT;

import com.tosi.user.common.exception.CustomException;
import com.tosi.user.common.exception.ExceptionCode;
import com.tosi.user.common.redis.entity.CacheKey;
import com.tosi.user.entity.User;
import com.tosi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * UserDetailServie 인터페이스를 구현한 클래스
     * loadUserByUsername 메서드를 오버라이드하여 UserDetail 대신 UserDetail을 구현한 CustomUserDetail 객체를 반환
     * 레디스에 username(이 프로젝트에서는 회원번호를 문자열로 변환한 값)을 키로 CustomUserDetails 객체를 캐싱함
     */

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = CacheKey.USER, key = "#username", unless = "#result == null")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long userId = Long.parseLong(username);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        return CustomUserDetails.builder()
                .username(user.getUserId().toString())
                .password(user.getPassword())
                //.authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())))
                .build();
    }

}

