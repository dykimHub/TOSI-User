package com.tosi.user.service;

import com.tosi.user.common.exception.SuccessResponse;
import com.tosi.user.dto.FavoriteDto;


public interface FavoriteService {

    SuccessResponse addFavorite(FavoriteDto favoriteDto);
}
