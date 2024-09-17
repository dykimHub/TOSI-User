package com.tosi.user.controller;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.FavoriteDto;
import com.tosi.user.dto.TaleDto;
import com.tosi.user.dto.UserDto;
import com.tosi.user.service.FavoriteService;
import com.tosi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/users/favorites")
@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final UserService userService;

    @Operation(summary = "동화 즐겨찾기에 추가")
    @PostMapping
    public ResponseEntity<SuccessResponse> addFavorite(@RequestHeader("Authorization") String accessToken, @RequestBody FavoriteDto favoriteDto) {
        userService.findUserDto(accessToken);
        SuccessResponse successResponse = favoriteService.addFavoriteTale(favoriteDto);
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
//
//    @GetMapping("/{taleId}")
//    public ResponseEntity<?> getFavorite(HttpServletRequest request, @PathVariable int taleId) {
//        try {
//            Integer userId = (Integer) request.getAttribute("userId");
//            int result = favoriteService.getFavorite(userId, taleId);
//            return new ResponseEntity<Integer>(result, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//    }
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


    //    @GetMapping
//    public ResponseEntity<?> getFavoritesList(HttpServletRequest request) {
//        try {
//            Integer userId = (Integer) request.getAttribute("userId");
//            List<TaleDto> favoriteList = favoriteService.getFavoriteList(userId);
//            return new ResponseEntity<List<TaleDto>>(favoriteList, HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }


}
