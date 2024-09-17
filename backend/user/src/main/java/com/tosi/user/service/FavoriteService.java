package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.TaleDto;


public interface FavoriteService {

    SuccessResponse addFavoriteTale(Long userId, Long taleId);

    TaleDto.TaleDtos findFavoriteTales(Long userId);

    boolean findFavoriteTale(Long userId, Long TaleId);

    SuccessResponse deleteFavoriteTale(Long userId, Long taleId);
}
