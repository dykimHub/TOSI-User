package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.TaleDto;
import org.springframework.data.domain.Pageable;


public interface FavoriteService {

    SuccessResponse addFavoriteTale(Long userId, Long taleId);

    TaleDto.TaleDtos findFavoriteTales(Long userId, Pageable pageable);

    boolean findFavoriteTale(Long userId, Long TaleId);

    SuccessResponse deleteFavoriteTale(Long userId, Long taleId);

    TaleDto.TaleDtos findPopularTales();
}
