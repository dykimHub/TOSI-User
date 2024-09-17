package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.TaleDto;
import com.tosi.user.entity.Favorite;
import com.tosi.user.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
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
     * 이미 즐겨찾기 목록에 있는 동화라면 추가하지 않습니다.
     * 회원(userId)의 동화 즐겨찾기 목록을 갱신하기 위해 캐시를 비웁니다.
     *
     * @param userId 회원 번호
     * @param taleId 동화 번호
     * @return 즐겨찾기에 성공하면 SuccessResponse 객체 반환
     */
    @CacheEvict(value = "favorites", key = "#userId")
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

        // 동화 프로젝트 API로 동화 번호를 조회해서 동화 객체를 리스트에 추가
        List<TaleDto> favoriteTaleDtoList = favoriteTaleIds.stream()
                .map(f -> restTemplate.getForObject(taleURL + f, TaleDto.class))
                .toList();

        // TaleDto 객체의 내부 클래스 TaleDtos 생성
        return new TaleDto.TaleDtos(favoriteTaleDtoList);

    }

    /**
     * 해당 동화가 이미 즐겨찾기 목록에 있는 동화인지의 여부를 판단하여 결과를 반환합니다.
     *
     * @param userId 회원 번호
     * @param taleId 동화 번호
     * @return 회원이 즐겨찾는 동화에 이미 있는 동화면 true, 아니면 false 반환
     */
    @Override
    public boolean findFavoriteTale(Long userId, Long taleId) {
        return favoriteRepository.existsByUserIdAndTaleId(userId, taleId);
    }

    /**
     * 해당 동화를 즐겨찾기 목록에서 삭제합니다.
     * 회원(userId)의 동화 즐겨찾기 목록을 갱신하기 위해 캐시를 비웁니다.
     *
     * @param userId 회원 번호
     * @param taleId 동화 번호
     * @return 삭제가 완료되면 SuccessResponse 객체를 반환
     */
    @CacheEvict(value = "favorites", key = "#userId")
    @Transactional
    @Override
    public SuccessResponse deleteFavoriteTale(Long userId, Long taleId) {
        favoriteRepository.deleteByUserIdAndTaleId(userId, taleId);
        return SuccessResponse.of("해당 동화가 즐겨찾기에서 성공적으로 삭제되었습니다.");
    }
}
