package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.ChildInfoDto;
import com.tosi.user.dto.UserInfoRequestDto;
import com.tosi.user.entity.Child;
import com.tosi.user.entity.User;
import com.tosi.user.repository.ChildRepository;
import com.tosi.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ChildRepository childRepository;

    /**
     * UserInfoRequestDto 객체의 회원 정보로 User 엔티티를 생성하고 users 테이블에 저장합니다.
     * UserInfoRequestDto 객체의 회원의 아이 목록 정보로 Child 엔티티를 각각 생성하고 children 테이블에 저장합니다.
     *
     * @param userInfoRequestDto 회원가입에 필요한 정보가 담긴 UserInfoRequestDto 객체
     * @return users, children 테이블 저장에 성공하면 SuccesResponse 객체를 반환
     */
    @Transactional
    public SuccessResponse addUser(UserInfoRequestDto userInfoRequestDto) {

        User user = User.builder()
                .email(userInfoRequestDto.getEmail())
                .password(userInfoRequestDto.getPassword())
                .nickname(userInfoRequestDto.getUserNickname())
                .bookshelfName(userInfoRequestDto.getUserNickname() + "님의 책장")
                .build();
        User newUser = userRepository.save(user);

        for (ChildInfoDto childInfoDto : userInfoRequestDto.getChildInfoDtoList()) {
            Child child = Child.builder()
                    .userId(newUser.getUserId())
                    .childName(childInfoDto.getChildName())
                    .childGender(childInfoDto.getChildGender())
                    .build();
            childRepository.save(child);

        }

        return SuccessResponse.of("회원가입이 성공적으로 완료되었습니다.");
    }

    /**
     * users 테이블에 이메일이 일치하는 회원이 있는지 확인합니다.
     *
     * @param email 예비 회원 이메일
     * @return users 테이블에 해당 이메일로 가입한 회원이 있으면 true, 아니면 false를 반환
     */
    @Override
    public boolean findUserEmailDuplication(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * users 테이블에 닉네임이 일치하는 회원이 있는지 확인합니다.
     *
     * @param nick 예비 회원 닉네임
     * @return users 테이블에 해당 닉네임으로 가입한 회원이 있으면 true, 아니면 false를 반환
     */
    @Override
    public boolean findUserNickNameDuplication(String nick) {
        return userRepository.existsByNickname(nick);
    }


//
//    // 회원 정보 조회
//    public User selectUser (int userId) {
//        Optional<User> user = userRepository.findById(userId);
//        return user.orElse(null);
//    }
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
//
//    // 로그인
//    public Integer login (LoginInfo loginInfo) {
//        User foundUser = userRepository.findByEmail(loginInfo.getEmail());
//        if(foundUser != null && foundUser.getPassword().equals(loginInfo.getPassword())) {
//            return foundUser.getUserId();
//        }
//        return null;
//    }
//
//    // 이메일 중복 확인
//    public boolean checkEmailDuplication (String email) {
//        User searchedUser = userRepository.findByEmail (email);
//        if (searchedUser != null){
//            return true;
//        }
//        return false;
//    }
//
//    // 비밀번호 확인
//    public boolean checkPassword (Integer userId, String password) {
//        Optional<User> optionalUser = userRepository.findById(userId);
//
//        if(optionalUser.isPresent()){
//            User user = optionalUser.get();
//            if(user.getPassword().equals(password)) {
//                return true;
//            }
//        }
//        return false;
//
//    }
//
//    // 아이 목록 조회
//    public List<Child> selectChildrenList(Integer userId) {
//
//        List<Child> childrenList = childRepository.findByUserIdOrderByMyBabyDescAndChildNameAsc(userId);
//        return childrenList;
//    }




//    public TokenInfo login(OriginLoginRequestDto dto) {
//        Member member = memberRepository.findByMemberId(dto.getMemberId()).orElseThrow(
//                () -> new NoSuchElementException("회원이 없습니다."));
//        checkPassword(dto.getMemberPass(), member.getPassword());
//
//        String username = member.getUsername();
//        String accessToken = jwtTokenUtil.generateAccessToken(username);
//        RefreshToken refreshToken = saveRefreshToken(username);
//        log.info("로그인 성공");
//        return TokenInfo.of(accessToken, refreshToken.getRefreshToken());
//    }
//
//    private void checkPassword(String rawPassword, String findMemberPassword) {
//        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//    }
//
//    private RefreshToken saveRefreshToken(String username) {
//        //log.info("RefreshToken 등록");
//        return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
//                jwtTokenUtil.generateRefreshToken(username), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
//    }
//
//    public MemberInfoDto getMemberInfo(String username) {
//        Member member = memberRepository.findByMemberId(username).orElseThrow(() -> new NoSuchElementException("회원이 없습니다."));
//
//        return MemberInfoDto.builder()
//                .username(member.getUsername())
//                .memberNickname(member.getMemberNickname())
//                .memberProfile(member.getMemberProfile())
//                .build();
//    }
//
//
//    @CacheEvict(value = CacheKey.USER, key = "#username")
//    public void logout(TokenInfo tokenDto, String username) {
//        String accessToken = resolveToken(tokenDto.getAccessToken());
//        long remainMilliSeconds = jwtTokenUtil
//                .getRemainMilliSeconds(accessToken);
//        refreshTokenRedisRepository.deleteById(username);
//        logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
//    }
//
//    private String resolveToken(String token) {
//        return token.substring(7);
//    }
//
//    public TokenInfo reissue(String refreshToken) {
//        refreshToken = resolveToken(refreshToken);
//        String username = getCurrentUsername();
//        RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username).orElseThrow(NoSuchElementException::new);
//
//        if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
//            return reissueRefreshToken(refreshToken, username);
//        }
//        throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
//    }
//
//    private TokenInfo reissueRefreshToken(String refreshToken, String username) {
//        if (lessThanReissueExpirationTimesLeft(refreshToken)) {
//            System.out.println("리프레시 토큰도 재발급");
//            return TokenInfo.of(jwtTokenUtil.generateAccessToken(username), saveRefreshToken(username).getRefreshToken());
//        }
//        System.out.println("엑세스 토큰만 재발급");
//        return TokenInfo.of(jwtTokenUtil.generateAccessToken(username), refreshToken);
//    }
//
//    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
//        return jwtTokenUtil.getRemainMilliSeconds(refreshToken) < JwtExpiration.REISSUE_EXPIRATION_TIME.getValue();
//    }
//
//    private String getCurrentUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
//        return principal.getUsername();
//    }
//
//    /**
//     * token에서 memberId를 찾아서 Member 객체를 반환함
//     *
//     * @param accessToken 로그인한 회원의 token
//     * @return 로그인한 Member 객체
//     * @throws CustomException 해당 Member 객체를 찾을 수 없음
//     */
//    public Member findMemberEntity(String accessToken) {
//        String memberId = jwtTokenUtil.getUsername(accessToken.substring(7));
//        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(ExceptionCode.MEMBER_NOT_FOUND));
//
//        return member;
//    }


}