package com.tosi.user.repository;

import com.tosi.user.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT f.taleId FROM Favorite f WHERE f.userId = :userId")
    List<Long> findTaleIdsByUserId(Long userId);

    boolean existsByUserIdAndTaleId(Long userId, Long taleId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.userId = :userId AND f.taleId = :taleId")
    int deleteByUserIdAndTaleId(Long userId, Long taleId);
}