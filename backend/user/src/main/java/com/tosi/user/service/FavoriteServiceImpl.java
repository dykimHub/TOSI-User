package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
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
     * 이미 회원 즐겨찾기 목록에 있는 동화라면 추가하지 않습니다.
     *
     * @param userId 회원 번호
     * @param taleId 동화 번호
     * @return 즐겨찾기에 성공하면 SuccessResponse 객체 반환
     */
    @Transactional
    @Override
    public SuccessResponse addFavoriteTale(Long userId, Long taleId) {
        Favorite favorite = Favorite.builder()
                .userId(userId)
                .taleId(taleId)
                .build();

        boolean exists = findFavoriteTale(favorite.getUserId(), favorite.getTaleId());
        if (exists) {
            return SuccessResponse.of("이미 즐겨찾기된 동화입니다.");
        }

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

        // 동화 프로젝트 API로 동화 번호를 조회해서 동화 객체를 리스트에 추가
        List<TaleDto> favoriteTaleDtoList = favoriteTaleIds.stream()
                .map(f -> restTemplate.getForObject(taleURL + f, TaleDto.class))
                .toList();

        // TaleDto 객체의 내부 클래스 TaleDtos 생성
        return new TaleDto.TaleDtos(favoriteTaleDtoList);

    }

    @Override
    public boolean findFavoriteTale(Long userId, Long taleId) {
        return favoriteRepository.existsByUserIdAndTaleId(userId, taleId);
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
