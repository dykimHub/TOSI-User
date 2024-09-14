package com.tosi.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FavoriteDto {

    private Integer favoriteId;

    private Integer userId;

    //private TaleDto taleDto;

}
