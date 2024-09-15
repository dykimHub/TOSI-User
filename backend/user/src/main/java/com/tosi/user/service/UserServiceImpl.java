package com.tosi.user.service;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.CustomException;
import com.tosi.user.common.exception.ExceptionCode;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.*;
import com.tosi.user.entity.Child;
import com.tosi.user.entity.User;
import com.tosi.user.entity.UserRole;
import com.tosi.user.repository.ChildRepository;
import com.tosi.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ChildRepository childRepository;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * UserInfoRequestDto 객체의 회원 정보로 User 엔티티를 생성하고 users 테이블에 저장합니다.
     * UserInfoRequestDto 객체의 회원의 아이 목록 정보로 Child 엔티티를 각각 생성하고 children 테이블에 저장합니다.
     *
     * @param joinDto 회원가입에 필요한 정보가 담긴 UserInfoRequestDto 객체
     * @return users, children 테이블 저장에 성공하면 SuccesResponse 객체를 반환
     */
    @Transactional
    public SuccessResponse addUser(JoinDto joinDto) {

        User user = User.builder()
                .email(joinDto.getEmail())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .nickname(joinDto.getNickname())
                .bookshelfName(joinDto.getBookshelfName())
                .role(UserRole.USER)
                .build();
        User newUser = userRepository.save(user);

        for (ChildDto childDto : joinDto.getChildren()) {
            Child child = Child.builder()
                    .userId(newUser.getUserId())
                    .childName(childDto.getChildName())
                    .childGender(childDto.getChildGender())
                    .build();
            childRepository.save(child);

        }

        return SuccessResponse.of("회원가입이 성공적으로 완료되었습니다.");
    }

    /**
     * 데이터베이스에 이메일이 일치하는 회원이 있는지 확인합니다.
     *
     * @param email 예비 회원 이메일
     * @return users 테이블에 해당 이메일로 가입한 회원이 있으면 true, 아니면 false를 반환
     */
    @Override
    public boolean findUserEmailDuplication(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 데이터베이스에 닉네임이 일치하는 회원이 있는지 확인합니다.
     *
     * @param nickname 예비 회원 닉네임
     * @return 데이터베이스에 해당 닉네임으로 가입한 회원이 있으면 true, 아니면 false를 반환
     */
    @Override
    public boolean findUserNickNameDuplication(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * LoginDto의 이메일로 회원을 조회한 후, 로그인 정보와 조회된 회원 객체를 매개변수로 토큰 생성 메서드를 호출합니다.
     *
     * @param loginDto 로그인 정보가 담긴 loginDto 객체
     * @return TokenInfo 생성된 엑세스 토큰과 리프레시 토큰이 담긴 객체
     * @throws CustomException 회원을 데이터베이스에서 찾을 수 없을 경우 발생
     */
    @Override
    public TokenInfo findUser(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        return authService.findTokenInfo(loginDto, user);
    }



//
//    @Transactional
//    // 회원 정보 수정
//    public void updateUser (UserInfo userInfo) {
//        Optional<User> optionalUser = userRepository.findById(userInfo.getUserId());
//
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            if(userInfo.getPassword() == null || userInfo.getPassword() == ""){
//                String password = user.getPassword();
//                userInfo.setPassword(password);
//            }
//            user.update(userInfo);
//        } else {
//            // 예외 던지기
//        }
//
//        childRepository.deleteByUserId(userInfo.getUserId());
//
//        List<ChildInfo> childrenList = userInfo.getChildrenList();
//
//        for (int i = 0; i < childrenList.size(); i++) {
//            childRepository.save(childrenList.get(i).toEntity(userInfo.getUserId()));
//        }
//    }
//
//    @Transactional
//    // 회원 탈퇴
//    public void deleteUser (int userId) {
//        refreshTokenRepository.deleteByUserId(userId);
//        customTaleRepository.deleteByUserId(userId);
//        favoriteRepository.deleteByUserId(userId);
//        childRepository.deleteByUserId(userId);
//        userRepository.deleteById(userId);
//    }

//    @CacheEvict(value = CacheKey.USER, key = "#username")
//    public void logout(TokenInfo tokenDto, String username) {
//        String accessToken = resolveToken(tokenDto.getAccessToken());
//        long remainMilliSeconds = jwtTokenUtil
//                .getRemainMilliSeconds(accessToken);
//        refreshTokenRedisRepository.deleteById(username);
//        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
//    }


}