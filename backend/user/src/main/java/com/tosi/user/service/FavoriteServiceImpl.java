package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.FavoriteDto;
import com.tosi.user.dto.TaleDto;
import com.tosi.user.entity.Favorite;
import com.tosi.user.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final RestTemplate restTemplate;

    @Value("${service.tale.url}")
    private String taleURL;

    /**
     * 회원 아이디와 동화 아이디를 이용해 해당 동화를 즐겨찾기에 추가합니다.
     *
     * @param favoriteDto 즐겨찾기할 회원과 동화 정보가 담긴 FavoriteDto 객체
     * @return 즐겨찾기에 성공하면 SuccessResponse 객체 반환
     */
    @Transactional
    @Override
    public SuccessResponse addFavoriteTale(FavoriteDto favoriteDto) {
        Favorite favorite = Favorite.builder()
                .userId(favoriteDto.getUserId())
                .taleId(favoriteDto.getTaleId())
                .build();

        favoriteRepository.save(favorite);

        return SuccessResponse.of("즐겨찾는 동화에 성공적으로 추가되었습니다.");
    }

    /**
     * 해당 회원의 동화 즐겨찾기 목록을 반환합니다.
     * 동화 즐겨찾기 목록을 캐시에 등록합니다.
     *
     * @param userId 회원 번호
     * @return TaleDto 객체 리스트를 감싼 TaleDtos 객체
     */
    @Cacheable(value = "favorites", key = "#userId")
    @Override
    public TaleDto.TaleDtos findFavoriteTales(Long userId) {
        // 회원이 즐겨찾기한 동화 번호 목록
        List<Long> favoriteTaleIds = favoriteRepository.findTaleIdsByUserId(userId);

        // 동화 프로젝트에 동화 번호를 조회해서 동화 객체를 리스트에 추가
        List<TaleDto> favoriteTaleDtoList = favoriteTaleIds.stream()
                .map(f -> restTemplate.getForObject(taleURL + f, TaleDto.class))
                .toList();

        return new TaleDto.TaleDtos(favoriteTaleDtoList);

    }

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
