package com.tosi.user.service;


import com.tosi.common.client.ApiClient;
import com.tosi.common.constants.ApiPaths;
import com.tosi.common.constants.CachePrefix;
import com.tosi.common.dto.TaleCacheDto;
import com.tosi.common.dto.TaleDetailCacheDto;
import com.tosi.common.exception.SuccessResponse;
import com.tosi.common.service.CacheService;
import com.tosi.user.entity.Favorite;
import com.tosi.user.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final CacheService cacheService;
    private final ApiClient apiClient;

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
        Favorite favorite = Favorite.builder()
                .userId(userId)
                .taleId(taleId)
                .build();

        boolean exists = findFavoriteTale(favorite.getUserId(), favorite.getTaleId());
        if (exists) {
            return SuccessResponse.of("이미 즐겨찾기된 동화입니다.");
        }

        favoriteRepository.save(favorite);
        cacheService.deleteCache(CachePrefix.FAVORITE_TALE.buildCacheKey(userId));

        return SuccessResponse.of("즐겨찾는 동화에 성공적으로 추가되었습니다.");
    }

    /**
     * 해당 회원의 동화 즐겨찾기 목록을 반환합니다.
     * 첫 페이지만 캐시를 활용하여 동화 ID를 조회합니다.
     * 조회한 동화 ID를 동화 서비스에 보내서 동화 개요를 반환 받습니다.
     *
     * @param userId   회원 번호
     * @param pageable 페이지 번호, 페이지 크기, 정렬 기준 및 방향을 담고 있는 Pageable 객체
     * @return TaleDto 객체 리스트
     */
    @Override
    public List<TaleCacheDto> findFavoriteTales(Long userId, Pageable pageable) {
        List<Long> favoriteTaleIds;

        // 1페이지라면 캐시에서 동화 ID를 조회하고, 없으면 DB에서 조회하여 캐시에 저장합니다.
        if (pageable.getPageNumber() == 0) {
            String cacheKey = CachePrefix.FAVORITE_TALE.buildCacheKey(userId);
            favoriteTaleIds = cacheService.getCache(cacheKey, List.class);

            if (favoriteTaleIds == null) {
                favoriteTaleIds = favoriteRepository.findByTaleIdsByUserId(userId, pageable).getContent();
                cacheService.setCache(cacheKey, favoriteTaleIds, 6, TimeUnit.HOURS);
            }
        }
        // 1페이지가 아니라면 DB에서 동화 ID를 조회합니다.
        else {
            favoriteTaleIds = favoriteRepository.findByTaleIdsByUserId(userId, pageable).getContent();
        }

        if (favoriteTaleIds.isEmpty())
            return Collections.emptyList();

        return apiClient.fetchObjectList(ApiPaths.MULTI_TALE.buildPath(taleURL, favoriteTaleIds), TaleCacheDto.class);

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
        cacheService.deleteCache(CachePrefix.FAVORITE_TALE.buildCacheKey(userId));
        return SuccessResponse.of("해당 동화가 즐겨찾기에서 성공적으로 삭제되었습니다.");
    }

    /**
     * 인기 동화 9개를 반환합니다.
     * 캐시에 인기 동화 ID가 존재하면 그대로 사용합니다.
     * 캐시에 없다면 DB에서 조회하여 관심도가 높은 9개의 동화 ID를 가져옵니다.
     * 가져온 ID를 동화 서비스에 보내 동화 개요 목록을 반환받습니다.
     *
     * @return TaleCacheDto 객체 리스트
     */
    @Override
    public List<TaleCacheDto> findPopularTales() {
        List<Long> popularTaleIds = cacheService.getCache(CachePrefix.POPULAR_TALE.getPrefix(), List.class);

        if (popularTaleIds == null) {
            popularTaleIds = favoriteRepository.findPopularTales();
            cacheService.setCache(CachePrefix.POPULAR_TALE.getPrefix(), popularTaleIds, 1, TimeUnit.HOURS); // 1시간 마다 순위 갱신
        }

        // 인기 동화는 상세 내용도 캐시에 저장합니다.
        apiClient.fetchObjectList(ApiPaths.MULTI_TALE_DETAIL.buildPath(taleURL, popularTaleIds), TaleDetailCacheDto.class);

        return apiClient.fetchObjectList(ApiPaths.MULTI_TALE.buildPath(taleURL, popularTaleIds), TaleCacheDto.class);

    }
}
