package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.FavoriteDto;
import com.tosi.user.dto.TaleDto;


public interface FavoriteService {

    SuccessResponse addFavoriteTale(FavoriteDto favoriteDto);

    TaleDto.TaleDtos findFavoriteTales(Long userId);
}
