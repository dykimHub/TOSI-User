package com.tosi.user.controller;


import com.tosi.common.dto.TaleCacheDto;
import com.tosi.common.exception.SuccessResponse;
import com.tosi.user.service.AuthService;
import com.tosi.user.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/users/favorites")
@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final AuthService authService;

    @Operation(summary = "동화 즐겨찾기에 추가")
    @PostMapping("/{taleId}")
    public ResponseEntity<SuccessResponse> addFavoriteTale(@RequestHeader("Authorization") String accessToken, @Parameter(example = "4") @PathVariable Long taleId) {
        Long userId = authService.findUserAuthorization(accessToken);
        SuccessResponse successResponse = favoriteService.addFavoriteTale(userId, taleId);
        return ResponseEntity.ok()
                .body(successResponse);

    }

    @Operation(summary = "동화 즐겨찾기 목록 조회")
    @GetMapping
    public ResponseEntity<List<TaleCacheDto>> findFavoriteTales(@RequestHeader("Authorization") String accessToken, @RequestParam(defaultValue = "0") int page) {
        Long userId = authService.findUserAuthorization(accessToken);
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "regDate"));
        List<TaleCacheDto> favoriteTaleCacheDtos = favoriteService.findFavoriteTales(userId, pageable);
        return ResponseEntity.ok()
                .body(favoriteTaleCacheDtos);

    }

    @Operation(summary = "동화 즐겨찾기 여부 조회")
    @GetMapping("/{taleId}")
    public ResponseEntity<Boolean> findFavoriteTale(@RequestHeader("Authorization") String accessToken, @Parameter(example = "4") @PathVariable Long taleId) {
        Long userId = authService.findUserAuthorization(accessToken);
        boolean exists = favoriteService.findFavoriteTale(userId, taleId);
        return ResponseEntity.ok()
                .body(exists);

    }

    @Operation(summary = "동화 즐겨찾기에서 삭제")
    @DeleteMapping("/{taleId}")
    public ResponseEntity<SuccessResponse> deleteFavoriteTale(@RequestHeader("Authorization") String accessToken, @Parameter(example = "4") @PathVariable Long taleId) {
        Long userId = authService.findUserAuthorization(accessToken);
        SuccessResponse successResponse = favoriteService.deleteFavoriteTale(userId, taleId);
        return ResponseEntity.ok()
                .body(successResponse);

    }

    @Operation(summary = "동화 인기순 조회")
    @GetMapping("/popular")
    public ResponseEntity<List<TaleCacheDto>> findPopularTales() {
        List<TaleCacheDto> favoriteTaleDtos = favoriteService.findPopularTales();
        return ResponseEntity.ok()
                .body(favoriteTaleDtos);

    }

}
