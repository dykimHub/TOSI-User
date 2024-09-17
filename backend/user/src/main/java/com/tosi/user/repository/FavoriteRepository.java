package com.tosi.user.repository;

import com.tosi.user.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Page<Favorite> findByUserId(Long userId, Pageable pageable);

    boolean existsByUserIdAndTaleId(Long userId, Long taleId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.userId = :userId AND f.taleId = :taleId")
    int deleteByUserIdAndTaleId(Long userId, Long taleId);

}