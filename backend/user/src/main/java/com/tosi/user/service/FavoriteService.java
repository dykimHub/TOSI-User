package com.tosi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FavoriteService {

//    private final FavoriteRepository favoriteRepository;
//    private final TaleDetailService taleDetailService;
//    private final S3Service s3Service;
//
//    // 즐겨찾기 삭제
//    public void deleteFavorite(Integer favoriteId) {
//        favoriteRepository.deleteById(favoriteId);
//    }
//
//    public Favorite insertFavorite(Favorite favorite) {
//        if (favorite == null)
//            throw new EntityNotFoundException();
//        return favoriteRepository.save(favorite);
//    }
//
//    public int getFavorite(int userId, int taleId) {
//        return favoriteRepository.getFavorite(userId, taleId);
//    }
//
//    public List<TaleDto> getFavoriteList(int userId) {
//        List<Favorite> favorites = favoriteRepository.getByUserId(userId);
//        if (favorites == null || favorites.size() == 0)
//            return new ArrayList<>();
//
//        List<TaleDto> favoriteTales = new ArrayList<>();
//        for (Favorite favorite : favorites)
//            favoriteTales.add(taleDetailService.getTaleDetail(favorite.getTaleId()));
//
//        return favoriteTales;
//    }


}
