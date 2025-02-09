package com.tosi.user.service;


import com.tosi.common.cache.TaleCacheDto;
import com.tosi.common.constants.ParameterKey;
import com.tosi.common.exception.SuccessResponse;
import com.tosi.user.entity.Favorite;
import com.tosi.user.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${service.tale.url}")
    private String taleURL;

    /**
     * 회원 아이디와 동화 아이디를 이용해 해당 동화를 즐겨찾기에 추가합니다.
     * 이미 즐겨찾기 목록에 있는 동화라면 추가하지 않습니다.
     *
     * @param userId 회원 번호
     * @param taleId 동화 번호
     * @return 즐겨찾기에 성공하면 SuccessResponse 객체 반환
     */
    @Transactional
    @Override
    public SuccessResponse addFavoriteTale(Long userId, Long taleId) {
        // 동화의 존재 여부를 API 요청을 통해 확인
        restTemplate.getForObject(taleURL + "/" + taleId, TaleCacheDto.class);

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
     *
     * @param userId   회원 번호
     * @param pageable 페이지 번호, 페이지 크기, 정렬 기준 및 방향을 담고 있는 Pageable 객체
     * @return TaleDto 객체 리스트
     */
    @Override
    public List<TaleCacheDto> findFavoriteTales(Long userId, Pageable pageable) {
        Page<Long> favoriteTaleIds = favoriteRepository.findByTaleIdsByUserId(userId, pageable);

        if (favoriteTaleIds.isEmpty())
            return Collections.emptyList();

        String requestURL = ParameterKey.TALE.buildQueryString(taleURL + "/bulk", favoriteTaleIds.getContent());

        return restTemplate.exchange( //  HTTP 응답을 적절한 타입으로 변환
                requestURL,
                HttpMethod.GET,
                null, // GET 요청이므로 요청 본문 없음
                new ParameterizedTypeReference<List<TaleCacheDto>>() { // 타입 정보 전달
                }
        ).getBody(); // ResponseEntity 응답에서 본문 추출

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
     *
     * @param userId 회원 번호
     * @param taleId 동화 번호
     * @return 삭제가 완료되면 SuccessResponse 객체를 반환
     */
    @Transactional
    @Override
    public SuccessResponse deleteFavoriteTale(Long userId, Long taleId) {
        favoriteRepository.deleteByUserIdAndTaleId(userId, taleId);
        return SuccessResponse.of("해당 동화가 즐겨찾기에서 성공적으로 삭제되었습니다.");
    }

    /**
     * 즐겨찾기가 많이 된 동화 순으로 9개를 반환합니다.
     * 인기순 동화(#PopularTaleListCache)를 캐시에 등록합니다.
     *
     * @return TaleDto 리스트를 감싼 TaleDtos 객체
     */
    @Cacheable(value = "PopularTaleListCache", key = "'PopularTaleListCache'")
    @Override
    public TaleCacheDto.TaleDtos findPopularTales() {
        return new TaleCacheDto.TaleDtos(favoriteRepository.findPopularTales().stream()
                .map(f -> restTemplate.getForObject(taleURL + "/" + f, TaleCacheDto.class))
                .toList()
        );
    }
}
