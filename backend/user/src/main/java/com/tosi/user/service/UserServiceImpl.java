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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
     * @return users, children 테이블 저장에 성공하면 SuccessResponse 객체를 반환
     */
    @Transactional
    public SuccessResponse join(JoinDto joinDto) {
        findUserEmailDuplication(joinDto.getEmail());
        findUserNickNameDuplication(joinDto.getNickname());

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

        return SuccessResponse.of("회원가입이 완료되었습니다.");
    }

    /**
     * 데이터베이스에 이메일이 일치하는 회원이 있는지 확인합니다.
     *
     * @param email 예비 회원 이메일
     * @return users 데이터베이스에 해당 이메일로 가입한 회원이 없으면 SuccessResponse 객체 반환
     * @throws CustomException 데이터베이스에 해당 이메일로 가입한 회원이 있음
     */
    @Override
    public SuccessResponse findUserEmailDuplication(String email) {
        if (userRepository.existsByEmail(email))
            throw new CustomException(ExceptionCode.EXISTED_EMAIL);
        return SuccessResponse.of("사용 가능한 이메일 입니다.");
    }

    /**
     * 데이터베이스에 닉네임이 일치하는 회원이 있는지 확인합니다.
     *
     * @param nickname 예비 회원 닉네임
     * @return 데이터베이스에 해당 닉네임으로 가입한 회원이 없으면 SuccessResponse 객체 반환
     * @throws CustomException 데이터베이스에 해당 닉네임으로 가입한 회원이 있음
     */
    @Override
    public SuccessResponse findUserNickNameDuplication(String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new CustomException(ExceptionCode.EXISTED_NICKNAME);
        return SuccessResponse.of("사용 가능한 닉네임 입니다.");
    }

    /**
     * LoginDto의 이메일로 회원을 조회한 후, 로그인 정보와 조회된 회원 객체를 매개변수로 토큰 생성 메서드를 호출합니다.
     *
     * @param loginDto 로그인 정보가 담긴 loginDto 객체
     * @return TokenInfo 생성된 엑세스 토큰과 리프레시 토큰이 담긴 객체
     * @throws CustomException 회원을 데이터베이스에서 찾을 수 없을 경우 발생
     */
    @Override
    public TokenInfo login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        return authService.findTokenInfo(loginDto, user);
    }

    /**
     * 로그아웃을 요청한 회원의 토큰을 무효화 시킵니다.
     *
     * @param tokenInfo 로그인한 회원의 엑세스 토큰과 리프레시 토큰이 담긴 객체
     * @param email 로그아웃을 시도한 회원의 이메일
     * @return 로그아웃에 성공하면 SuccessResponse 객체 반환
     */
    @Override
    public SuccessResponse logout(TokenInfo tokenInfo, String email) {
        authService.invalidateToken(tokenInfo, email);
        return SuccessResponse.of("로그아웃 되었습니다.");
    }

    /**
     * 토큰에서 추출된 회원 이메일을 조회하여 UserDTO 객체로 변환하여 반환합니다.
     *
     * @param accessToken 로그인한 회원의 토큰
     * @return UserDto 객체
     */
    @Override
    public UserDto findUserDto(String accessToken) {
        String email = authService.findUserAuthorization(accessToken);
        return userRepository.findUserDtoByEmail(email)
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
    }

    /**
     * 이메일로 회원 정보를 조회한 후, 자녀 데이터베이스에서 해당 회원의 자녀 목록을 조회하여 반환합니다.
     *
     * @param userDto 로그인한 회원의 UserDto 객체
     * @return 회원 정보와 회원이 등록한 자녀의 정보가 담긴 UserNChildDto 객체
     */
    @Cacheable(value = "userNchild", key = "#userDto.userId")
    @Override
    public UserNChildrenDto findUserNChildren(UserDto userDto) {
        List<ChildDto> children = userRepository.findChildrenDtoByUserId(userDto.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.CHILDREN_NOT_FOUND));

        return UserNChildrenDto.builder()
                .userDto(userDto)
                .children(children)
                .build();
    }

    /**
     * 회원의 닉네임 혹은 책장 이름을 수정합니다.
     *
     * @param modifyingUserDto 수정할 정보가 담긴 UserDto 객체
     * @return 수정이 완료되면 SuccessResponse 반환
     * @throws CustomException 이미 있는 닉네임으로 바꾸려고 하면 예외 처리
     */
    @CacheEvict(value = "userNchild", key = "#modifyingUserDto.userId")
    @Transactional
    @Override
    public SuccessResponse updateUser(UserDto modifyingUserDto) {
        if (userRepository.existsByNickname(modifyingUserDto.getNickname()))
            throw new CustomException(ExceptionCode.EXISTED_NICKNAME);

        userRepository.modifyUser(modifyingUserDto);

        return SuccessResponse.of("회원 정보가 성공적으로 수정되었습니다.");
    }

    /**
     *
     * @param userId 회원 번호
     * @param childDto
     * @return
     */
    @CacheEvict(value = "userNchild", key = "#userId")
    @Transactional
    @Override
    public SuccessResponse addChild(Long userId, ChildDto childDto) {
        Child child = Child.builder()
                .userId(userId)
                .childName(childDto.getChildName())
                .childGender(childDto.getChildGender())
                .build();

        childRepository.save(child);

        return SuccessResponse.of("자녀 목록에 성공적으로 추가되었습니다.");
    }

    @CacheEvict(value = "userNchild", key = "#userId")
    @Transactional
    @Override
    public SuccessResponse deleteChild(Long userId, Long childId) {
        childRepository.deleteById(childId);
        return SuccessResponse.of("해당 자녀가 성공적으로 삭제되었습니다.");
    }

}