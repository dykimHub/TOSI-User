package com.tosi.user.controller;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.*;
import com.tosi.user.service.AuthService;
import com.tosi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<SuccessResponse> join(@RequestBody JoinDto joinDto) {
        SuccessResponse successResponse = userService.join(joinDto);
        return ResponseEntity.ok()
                .body(successResponse);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody LoginDto loginDto) {
        TokenInfo tokenInfo = userService.login(loginDto);
        return ResponseEntity.ok()
                .body(tokenInfo);
    }

    @Operation(summary = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(@RequestHeader("Authorization") String accessToken,  @RequestHeader("RefreshToken") String refreshToken) {
        UserDto userDto = userService.findUserDto(accessToken);
        SuccessResponse successResponse = authService.invalidateToken(TokenInfo.of(accessToken, refreshToken), userDto.getEmail());
        return ResponseEntity.ok()
                .body(successResponse);
    }

    @Operation(summary = "리프레시 토큰 재발급")
    @GetMapping("/reissue")
    public ResponseEntity<TokenInfo> reissue(@RequestHeader("RefreshToken") String refreshToken) {
        TokenInfo tokenInfo = authService.reissue(refreshToken);
        return ResponseEntity.ok()
                .body(tokenInfo);
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

    @Operation(summary = "자녀 추가")
    @PostMapping("/child")
    public ResponseEntity<SuccessResponse> addChild(@RequestHeader("Authorization") String accessToken, @RequestBody ChildDto childDto) {
        UserDto userDto = userService.findUserDto(accessToken);
        SuccessResponse successResponse = userService.addChild(userDto.getUserId(), childDto);
        return ResponseEntity.ok()
                .body(successResponse);
    }

    @Operation(summary = "자녀 삭제")
    @DeleteMapping("/{childId}")
    public ResponseEntity<SuccessResponse> deleteChild(@RequestHeader("Authorization") String accessToken, @PathVariable Long childId) {
        UserDto userDto = userService.findUserDto(accessToken);
        SuccessResponse successResponse = userService.deleteChild(userDto.getUserId(), childId);
        return ResponseEntity.ok()
                .body(successResponse);
    }




}
