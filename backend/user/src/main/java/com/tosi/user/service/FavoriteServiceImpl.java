package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.FavoriteDto;
import com.tosi.user.entity.Favorite;
import com.tosi.user.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements FavoriteService{
    private final FavoriteRepository favoriteRepository;

    /**
     * 회원 아이디와 동화 아이디를 이용해 해당 동화를 즐겨찾기에 추가합니다.
     *
     * @param favoriteDto 즐겨찾기할 회원과 동화 정보가 담긴 FavoriteDto 객체
     * @return 즐겨찾기에 성공하면 SuccessResponse 객체 반환
     */
    @Transactional
    @Override
    public SuccessResponse addFavorite(FavoriteDto favoriteDto) {
        Favorite favorite = Favorite.builder()
                .userId(favoriteDto.getUserId())
                .taleId(favoriteDto.getTaleId())
                .build();

        favoriteRepository.save(favorite);

        return SuccessResponse.of("즐겨찾는 동화에 성공적으로 추가되었습니다.");
    }

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
