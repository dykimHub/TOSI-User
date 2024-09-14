package com.tosi.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/favorites")
@RestController
public class FavoriteController {

//    private final FavoriteService favoriteService;
//    private final TaleDetailService taleDetailService;
//
//    @PostMapping
//    public ResponseEntity<?> postFavorite(HttpServletRequest request, @RequestBody Favorite favorite) {
//        try {
//            Integer userId = (Integer) request.getAttribute("userId");
//            favorite.setUserId(userId);
//            Favorite savedFavorite = favoriteService.insertFavorite(favorite);
//            return new ResponseEntity<Favorite>(savedFavorite, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//    }
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
//
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
