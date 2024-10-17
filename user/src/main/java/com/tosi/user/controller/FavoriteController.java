package com.tosi.user.controller;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.TaleDto;
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
    public ResponseEntity<TaleDto.TaleDtos> findFavoriteTales(@RequestHeader("Authorization") String accessToken, @RequestParam(defaultValue = "0") int page) {
        Long userId = authService.findUserAuthorization(accessToken);
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "regDate"));
        TaleDto.TaleDtos favoriteTaleDtos = favoriteService.findFavoriteTales(userId, pageable);
        return ResponseEntity.ok()
                .body(favoriteTaleDtos);

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
    public ResponseEntity<TaleDto.TaleDtos> findPopularTales() {
        TaleDto.TaleDtos favoriteTaleDtos = favoriteService.findPopularTales();
        return ResponseEntity.ok()
                .body(favoriteTaleDtos);

    }

}
