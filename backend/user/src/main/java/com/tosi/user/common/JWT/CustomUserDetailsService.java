package com.tosi.user.common.JWT;

import com.tosi.user.common.redis.entity.CacheKey;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

//    private final MemberRepository memberRepository;

    @Override
    @Cacheable(value = CacheKey.USER, key = "#username", unless = "#result == null")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //System.out.println("유저 로드 시작");
        //Member member = memberRepository.findByMemberId(username)
                //.orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));
        //System.out.println("member 정보: "+ member.toString());
        //return Member.of(member);
        return null;
    }
}
