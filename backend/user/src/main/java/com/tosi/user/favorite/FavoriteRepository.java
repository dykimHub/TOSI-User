package com.tosi.user.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    @Query("SELECT f.favoriteId FROM Favorite f WHERE f.userId=:userId AND f.taleId=:taleId")
    int getFavorite(int userId, int taleId);

    @Query("SELECT f FROM Favorite f WHERE f.userId=:userId")
    List<Favorite> getByUserId(int userId);

    void deleteByUserId(int userId);
}