package com.tosi.user.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByUserId(Integer userId);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("update RefreshToken r set r.refreshToken = :refreshToken where r.userId = :userId")
    void updateRefreshTokenByUserId(Integer userId);
    @Modifying
    @Query("delete from RefreshToken r where r.userId = :userId")
    void deleteByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query("delete from RefreshToken r where r.refreshToken = :refreshToken")
    void deleteByRefreshToken(String refreshToken);

}
