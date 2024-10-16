package com.tosi.user.controller;

import com.tosi.user.common.JWT.TokenInfo;
import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.*;
import com.tosi.user.service.AuthService;
import com.tosi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = ExampleObject.join
                    )
            )
    )
    @PostMapping
    public ResponseEntity<SuccessResponse> join(@RequestBody JoinDto joinDto) {
        SuccessResponse successResponse = userService.join(joinDto);
        return ResponseEntity.ok()
                .body(successResponse);
    }

    @Operation(summary = "로그인")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = ExampleObject.login
                    )
            )
    )
    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody LoginDto loginDto) {
        TokenInfo tokenInfo = userService.login(loginDto);
        return ResponseEntity.ok()
                .body(tokenInfo);
    }

    @Operation(summary = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(@RequestHeader("Authorization") String accessToken, @RequestHeader("RefreshToken") String refreshToken) {
        Long userId = authService.findUserAuthorization(accessToken);
        SuccessResponse successResponse = authService.invalidateToken(TokenInfo.of(accessToken, refreshToken), userId.toString());
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

    @Operation(summary = "JWT 토큰 인증")
    @GetMapping("/auth")
    public ResponseEntity<Long> findUserAuthorization(@RequestHeader("Authorization") String accessToken) {
        Long userId = authService.findUserAuthorization(accessToken);
        return ResponseEntity.ok()
                .body(userId);
    }

    @Operation(summary = "회원 정보 상세(자녀 포함)")
    @GetMapping
    public ResponseEntity<UserNChildrenDto> findUserNChildren(@RequestHeader("Authorization") String accessToken) {
        Long userId = authService.findUserAuthorization(accessToken);
        UserNChildrenDto userNChildrenDto = userService.findUserNChildren(userId);
        return ResponseEntity.ok()
                .body(userNChildrenDto);
    }

    @Operation(summary = "회원 정보 수정")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = ExampleObject.modify
                    )
            )
    )
    @PutMapping
    public ResponseEntity<SuccessResponse> modifyUser(@RequestHeader("Authorization") String accessToken, @RequestBody ModifyingUserDto modifyingUserDto) {
        Long userId = authService.findUserAuthorization(accessToken);
        SuccessResponse successResponse = userService.updateUser(userId, modifyingUserDto);
        return ResponseEntity.ok()
                .body(successResponse);
    }

    @Operation(summary = "회원 자녀 추가")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = ExampleObject.child
                    )
            )
    )
    @PostMapping("/child")
    public ResponseEntity<SuccessResponse> addChild(@RequestHeader("Authorization") String accessToken, @RequestBody ChildDto childDto) {
        Long userId = authService.findUserAuthorization(accessToken);
        SuccessResponse successResponse = userService.addChild(userId, childDto);
        return ResponseEntity.ok()
                .body(successResponse);
    }

    @Operation(summary = "회원 자녀 삭제")
    @DeleteMapping("/child/{childId}")
    public ResponseEntity<SuccessResponse> deleteChild(@RequestHeader("Authorization") String accessToken, @PathVariable Long childId) {
        Long userId = authService.findUserAuthorization(accessToken);
        SuccessResponse successResponse = userService.deleteChild(userId, childId);
        return ResponseEntity.ok()
                .body(successResponse);
    }


}
