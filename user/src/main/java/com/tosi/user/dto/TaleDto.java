package com.tosi.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDto {
    private long taleId;
    private String title;
    private String thumbnailS3Key;
    private String thumbnailS3URL;
    private int ttsLength;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TaleDtos {
        private List<TaleDto> taleDtos;

        public TaleDtos(List<TaleDto> taleDtos) {
            this.taleDtos = taleDtos;
        }
    }

}
