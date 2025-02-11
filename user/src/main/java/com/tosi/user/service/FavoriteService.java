package com.tosi.user.service;


import com.tosi.common.cache.TaleCacheDto;
import com.tosi.common.exception.SuccessResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface FavoriteService {

    SuccessResponse addFavoriteTale(Long userId, Long taleId);

    List<TaleCacheDto> findFavoriteTales(Long userId, Pageable pageable);

    boolean findFavoriteTale(Long userId, Long TaleId);

    SuccessResponse deleteFavoriteTale(Long userId, Long taleId);

    List<TaleCacheDto> findPopularTales();
}
