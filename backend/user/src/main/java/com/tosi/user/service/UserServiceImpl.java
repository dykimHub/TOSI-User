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
     * 토큰에서 추출한 회원 번호를 Long 타입으로 변환합니다.
     *
     * @param accessToken 로그인한 회원의 토큰
     * @return 토큰을 만들 때 String 타입으로 변환한 회원 번호를 Long 타입으로 변환하여 반환
     */
    @Override
    public Long findUserId(String accessToken) {
        String userId = authService.findUserAuthorization(accessToken);
        if (userId == null)
            throw new CustomException(ExceptionCode.USER_NOT_FOUND);
        return Long.parseLong(userId);
    }

    /**
     * 토큰에서 추출된 회원 번호를 조회하여 UserDTO 객체로 변환하여 반환합니다.
     *
     * @param accessToken 로그인한 회원의 토큰
     * @return UserDto 객체
     */
    @Override
    public UserDto findUserDto(String accessToken) {
        String userId = authService.findUserAuthorization(accessToken);
        return userRepository.findUserDtoById(Long.parseLong(userId))
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
    }

    /**
     * 이메일로 회원 정보를 조회한 후, 자녀 데이터베이스에서 해당 회원의 자녀 목록을 조회하여 반환합니다.
     * 회원과 회원의 자녀 정보(#회원번호)를 캐시에 등록합니다.
     *
     * @param userDto 로그인한 회원의 UserDto 객체
     * @return 회원 정보와 회원이 등록한 자녀의 정보가 담긴 UserNChildDto 객체
     */
    @Cacheable(value = "userNchild", key = "#userDto.userId")
    @Override
    public UserNChildrenDto findUserNChildren(UserDto userDto) {
        List<ChildDto> children = userRepository.findChildrenDtoById(userDto.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.CHILDREN_NOT_FOUND));

        return UserNChildrenDto.builder()
                .userDto(userDto)
                .children(children)
                .build();
    }

    /**
     * 회원의 닉네임 혹은 책장 이름을 수정합니다.
     * 회원(#회원번호)의 회원 정보를 갱신하기 위해 캐시를 비웁니다.
     *
     * @param userDto          기존 UserDto 객체
     * @param modifyingUserDto 수정할 정보가 담긴 UserDto 객체
     * @return 수정이 완료되면 SuccessResponse 반환
     * @throws CustomException 이미 있는 닉네임으로 바꾸려고 하면 예외 처리
     */
    @CacheEvict(value = "userNchild", key = "#userDto.userId")
    @Transactional
    @Override
    public SuccessResponse updateUser(UserDto userDto, ModifyingUserDto modifyingUserDto) {
        if (!userDto.getNickname().equals(modifyingUserDto.getNickname()) // 닉네임이 변경되었는데(기존 닉네임과 다른데)
                && userRepository.existsByNickname(modifyingUserDto.getNickname())) // 변경된 닉네임이 이미 존재하는 닉네임이라면
            throw new CustomException(ExceptionCode.EXISTED_NICKNAME);

        userRepository.modifyUser(userDto.getUserId(), modifyingUserDto);

        return SuccessResponse.of("회원 정보가 성공적으로 수정되었습니다.");
    }

    /**
     * 회원 번호와 자녀 정보를 기반으로 자녀 데이터를 데이터베이스에 추가합니다.
     * 회원(#회원번호)의 자녀 목록을 갱신하기 위해 캐시를 비웁니다.
     *
     * @param userId   회원 번호
     * @param childDto 등록할 자녀의 정보가 담긴 ChildDto 객체
     * @return 자녀 등록에 성공하면 SuccessResponse를 반환합니다.
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

    /**
     * 회원이 등록한 자녀를 삭제합니다.
     * 회원(#회원번호)의 자녀 목록을 갱신하기 위해 캐시를 비웁니다.
     *
     * @param userId  회원 번호
     * @param childId 자녀 번호
     * @return 자녀가 삭제되면 SuccessResponse 객체 반환
     */
    @CacheEvict(value = "userNchild", key = "#userId")
    @Transactional
    @Override
    public SuccessResponse deleteChild(Long userId, Long childId) {
        childRepository.deleteById(childId);
        return SuccessResponse.of("해당 자녀가 성공적으로 삭제되었습니다.");
    }


}