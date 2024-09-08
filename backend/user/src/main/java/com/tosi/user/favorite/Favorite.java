package com.tosi.user.favorite;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name = "favorites")
@Data
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer favoriteId;

    @Column(nullable = false)
    private Integer userId;

    @Column
    private int taleId;

    @Column(insertable = false)
    private LocalDateTime regDate;

}
