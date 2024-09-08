package com.tosi.user.favorite;

import com.ssafy.tosi.tale.TaleDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FavoriteDto {

    private Integer favoriteId;

    private Integer userId;

    private TaleDto taleDto;

}
