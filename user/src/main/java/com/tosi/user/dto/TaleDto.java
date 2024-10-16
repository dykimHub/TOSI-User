package com.tosi.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash("TaleDto") 
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDto {

    @Id // Redis 고유 식별자
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
