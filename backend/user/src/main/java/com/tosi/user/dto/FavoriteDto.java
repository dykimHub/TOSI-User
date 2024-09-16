package com.tosi.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteDto {

    private Long favoriteId;
    private Long userId;
    private Long taleId;

    @Builder
    public FavoriteDto(Long userId, Long taleId) {
        this.userId = userId;
        this.taleId = taleId;
    }
}
