package com.tosi.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "tale_id"})
})
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id", nullable = false)
    private Long favoriteId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tale_id", nullable = false)
    private Long taleId;

    @CreationTimestamp
    @Column(name ="reg_date", nullable = false)
    private OffsetDateTime regDate;

    @Builder
    public Favorite(Long userId, Long taleId) {
        this.userId = userId;
        this.taleId = taleId;
    }
}
