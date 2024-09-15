package com.tosi.user.controller;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.LoginDto;
import com.tosi.user.dto.JoinDto;
import com.tosi.user.dto.UserNChildrenDto;
import com.tosi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<SuccessResponse> addUser(@RequestBody JoinDto joinDto) {
        SuccessResponse successResponse = userService.addUser(joinDto);
        return ResponseEntity.ok()
                .body(successResponse);
    }

    @Operation(summary = "이메일 중복 체크")
    @GetMapping("/emaildup")
    public ResponseEntity<Boolean> findUserEmailDuplication(@RequestParam String email) {
        boolean isEmailDup = userService.findUserEmailDuplication(email);
        return ResponseEntity.ok()
                .body(isEmailDup);
    }

    @Operation(summary = "닉네임 중복 체크")
    @GetMapping("/nickdup")
    public ResponseEntity<Boolean> findUserNicknameDuplication(@RequestParam String nickname) {
        boolean isNicknameDup = userService.findUserNickNameDuplication(nickname);
        return ResponseEntity.ok()
                .body(isNicknameDup);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenInfo> findUser(@RequestBody LoginDto loginDto) {
        TokenInfo tokenInfo = userService.findUser(loginDto);
        return ResponseEntity.ok()
                .body(tokenInfo);
    }

    @Operation(summary = "회원 상세")
    @GetMapping
    public ResponseEntity<UserNChildrenDto> findUserChildren(@RequestHeader("Authorization") String accessToken) {
        UserNChildrenDto userNChildrenDto = userService.findUserChildren(accessToken);
        return ResponseEntity.ok()
                .body(userNChildrenDto);
    }

//    // 회원 정보 조회
//    @GetMapping
//    public ResponseEntity<UserInfoResponse> getUser(HttpServletRequest request) {
//
//        Integer userId = (Integer) request.getAttribute("userId");
//        User user = userService.selectUser(userId);
//        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
//                .email(user.getEmail())
//                .bookshelfName((user.getBookshelfName()))
//                .childrenList(user.getChildrenList())
//                .build();
//
//        return new ResponseEntity<UserInfoResponse>(userInfoResponse, HttpStatus.OK);
//    }
//
//    // 회원 정보 수정
//    @PutMapping
//    public ResponseEntity<Void> putUser(HttpServletRequest request, @RequestBody UserInfo userInfo) {
//        Integer userId = (Integer) request.getAttribute("userId");
//        userInfo.setUserId(userId);
//
//        userService.updateUser(userInfo);
//
//        return new ResponseEntity<Void>(HttpStatus.OK);
//    }
//
//    // 회원 탈퇴
//    @DeleteMapping
//    public ResponseEntity<Void> deleteUser(HttpServletRequest request, HttpServletResponse response) {
//        Integer userId = (Integer) request.getAttribute("userId");
//        userService.deleteUser(userId);
//
//        cookieUtil.deleteCookie(request, response, "access-token");
//        cookieUtil.deleteCookie(request, response, "refresh-token");
//
//        return new ResponseEntity<Void>(HttpStatus.OK);
//    }
//
//    // 로그인
//    @PostMapping("/login")
//    public ResponseEntity<?> postLogin(@RequestBody LoginInfo loginInfo, HttpServletResponse response, HttpSession session) {
//
//        Integer userId = userService.login(loginInfo);
//
//        if (userId == null) {
//            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//        }
//
//        if (loginInfo.getAutoLogin() == false) {
//            session.setAttribute("isLoggedIn", "true");
//            Map<String, String> map = new HashMap<>();
//            map.put("access-token", jwtUtil.generateToken(userId, Duration.ofDays(1)));
//            return new ResponseEntity<Map>(map, HttpStatus.ACCEPTED);
//        }
//
//        String accessToken = jwtUtil.generateToken(userId, Duration.ofDays(1));
//        String refreshToken = jwtUtil.generateToken(userId, Duration.ofDays(7));
//        RefreshToken refreshTokenEntity = RefreshToken.builder().userId(userId).refreshToken(refreshToken).build();
//        refreshTokenService.updateRefreshToken(refreshTokenEntity);
//
//        Map<String, String> map = new HashMap<>();
//        map.put("access-token", accessToken);
//        map.put("refresh-token", refreshToken);
//
//        return new ResponseEntity<Map>(map, HttpStatus.ACCEPTED);
//    }
//
//    // 로그아웃
//    @GetMapping("/logout")
//    public ResponseEntity<Void> getLogout(HttpServletRequest request, HttpServletResponse response) {
//        cookieUtil.deleteCookie(request, response, "access-token");
//        cookieUtil.deleteCookie(request, response, "refresh-token");
//        return new ResponseEntity<Void>(HttpStatus.OK);
//    }
//
//    // 이메일 중복 검색
//    @GetMapping("/email-check")
//    public ResponseEntity<Boolean> getEmailCheck(@RequestParam String email) {
//        boolean result = userService.checkEmailDuplication(email);
//        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
//    }
//
//    // 아이목록 조회
//    @GetMapping("/children-list")
//    public ResponseEntity<List<Child>> getChildrenList(HttpServletRequest request) {
//        Integer userId = (Integer) request.getAttribute("userId");
//        List<Child> childrenList = userService.selectChildrenList(userId);
//        return new ResponseEntity<List<Child>>(childrenList, HttpStatus.OK);
//    }
//
//    // 비밀번호 확인
//    @PostMapping("/password-check")
//    public ResponseEntity<?> postPasswordCheck(HttpServletRequest request, @RequestBody Password password) {
//        Integer userId = (Integer) request.getAttribute("userId");
//
//        boolean result = userService.checkPassword(userId, password.getPassword());
//        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
//    }
}
