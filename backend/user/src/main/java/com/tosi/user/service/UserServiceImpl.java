package com.tosi.user.service;

import com.tosi.user.dto.UserInfoRequest;
import com.tosi.user.entity.User;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    public SuccessResponse postUser(UserInfoRequest userInfoRequest) {
        User user = User.builder()
                .email(userInfoRequest.getEmail())
                .password(userInfoRequest.getPassword())
                .userNickname(userInfoRequest.getUserNickname())
                .bookshelfName(userInfoRequest.getUserNickname() + "님의 책장")
                .build();
        userRepository.save(user);

        return SuccessResponse.of("회원가입이 성공적으로 완료되었습니다.");
    }

//    // 회원 가입
//    @Transactional
//    public int insertUser (UserInfo userInfo) {
//
//        User user = userInfo.toEntity();
//
//        User insertedUser = userRepository.save(user);
//
//        List<ChildInfo> childrenList = userInfo.getChildrenList();
//
//        for (int i = 0; i < childrenList.size(); i++) {
//            Child child = childrenList.get(i).toEntity(insertedUser.getUserId());
//            childRepository.save(child);
//        }
//        return insertedUser.getUserId();
//    }
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


//    public boolean checkDup(String category, String input) {
//        //category로 id, nickname의 값이 넘어오면 해당하는 중복검사 실행
//        //true = 해당하는 값이 이미 있음
//        boolean check = switch (category) {
//            case "id" -> memberRepository.existsByMemberId(input);
//
//            case "nickname" -> memberRepository.existsByMemberNickname(input);
//
//            default -> throw new IllegalStateException("올바른 category가 아닙니다. ");
//        };
//        return check;
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