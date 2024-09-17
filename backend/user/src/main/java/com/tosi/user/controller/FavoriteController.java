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
    public ResponseEntity<SuccessResponse> addFavorite(@RequestHeader("Authorization") String accessToken, @PathVariable Long taleId) {
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
//
//    @DeleteMapping("/{favoriteId}")
//    public ResponseEntity<?> deleteFavorite(HttpServletRequest request, @PathVariable int favoriteId) {
//        try {
//            favoriteService.deleteFavorite(favoriteId);
//            return new ResponseEntity<Void>(HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }


}
