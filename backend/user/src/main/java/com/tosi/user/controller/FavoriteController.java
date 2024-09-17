package com.tosi.user.controller;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.TaleDto;
import com.tosi.user.dto.UserDto;
import com.tosi.user.service.FavoriteService;
import com.tosi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users/favorites")
@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final UserService userService;

    @Operation(summary = "동화 즐겨찾기에 추가")
    @PostMapping("/{taleId}")
    public ResponseEntity<SuccessResponse> addFavoriteTale(
            @RequestHeader("Authorization") String accessToken, @PathVariable Long taleId) {
        UserDto userDto = userService.findUserDto(accessToken);
        SuccessResponse successResponse = favoriteService.addFavoriteTale(userDto.getUserId(), taleId);
        return ResponseEntity.ok()
                .body(successResponse);

    }

    @Operation(summary = "동화 즐겨찾기 목록 조회")
    @GetMapping
    public ResponseEntity<TaleDto.TaleDtos> findFavoriteTales(@RequestHeader("Authorization") String accessToken) {
        UserDto userDto = userService.findUserDto(accessToken);
        TaleDto.TaleDtos favoriteTaleDtos = favoriteService.findFavoriteTales(userDto.getUserId());
        return ResponseEntity.ok()
                .body(favoriteTaleDtos);

    }

    @Operation(summary = "동화 즐겨찾기 여부 조회")
    @GetMapping("/{taleId}")
    public ResponseEntity<Boolean> findFavoriteTale(@RequestHeader("Authorization") String accessToken, @PathVariable Long taleId) {
        UserDto userDto = userService.findUserDto(accessToken);
        boolean exists = favoriteService.findFavoriteTale(userDto.getUserId(), taleId);
        return ResponseEntity.ok()
                .body(exists);

    }

    @Operation(summary = "동화 즐겨찾기에서 삭제")
    @DeleteMapping("/{taleId}")
    public ResponseEntity<SuccessResponse> deleteFavoriteTale(@RequestHeader("Authorization") String accessToken, @PathVariable Long taleId) {
        UserDto userDto = userService.findUserDto(accessToken);
        SuccessResponse successResponse = favoriteService.deleteFavoriteTale(userDto.getUserId(), taleId);
        return ResponseEntity.ok()
                .body(successResponse);

    }

}
