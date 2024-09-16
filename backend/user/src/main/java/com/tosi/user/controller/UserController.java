package com.tosi.user.controller;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.*;
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
    public ResponseEntity<SuccessResponse> findUserEmailDuplication(@RequestParam String email) {
        SuccessResponse uniqueEmail = userService.findUserEmailDuplication(email);
        return ResponseEntity.ok()
                .body(uniqueEmail);
    }

    @Operation(summary = "닉네임 중복 체크")
    @GetMapping("/nickdup")
    public ResponseEntity<SuccessResponse> findUserNicknameDuplication(@RequestParam String nickname) {
        SuccessResponse uniqueNickname = userService.findUserNickNameDuplication(nickname);
        return ResponseEntity.ok()
                .body(uniqueNickname);
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
    public ResponseEntity<UserNChildrenDto> findUserNChildren(@RequestHeader("Authorization") String accessToken) {
        UserDto userDto = userService.findUserDto(accessToken);
        UserNChildrenDto userNChildrenDto = userService.findUserNChildren(userDto);
        return ResponseEntity.ok()
                .body(userNChildrenDto);
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping
    public ResponseEntity<SuccessResponse> modifyUser(@RequestHeader("Authorization") String accessToken, @RequestBody UserDto modifyingUserDto) {
        userService.findUserDto(accessToken);
        SuccessResponse successResponse = userService.updateUser(modifyingUserDto);
        return ResponseEntity.ok()
                .body(successResponse);
    }


//
//
//    // 로그아웃
//    @GetMapping("/logout")
//    public ResponseEntity<Void> getLogout(HttpServletRequest request, HttpServletResponse response) {
//        cookieUtil.deleteCookie(request, response, "access-token");
//        cookieUtil.deleteCookie(request, response, "refresh-token");
//        return new ResponseEntity<Void>(HttpStatus.OK);
//    }
//
//
}
