package com.tosi.user.repository;

import com.tosi.user.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * 특정 회원 번호에 해당하는 Favorite 엔티티의 동화 번호 리스트를 반환합니다.
     *
     * @param userId 회원 번호
     * @return Favorite 엔티티의 동화 번호 리스트
     */
    @Query("SELECT f.taleId FROM Favorite f WHERE f.userId = :userId")
    List<Long> findTaleIdsByUserId(Long userId);

    boolean existsByUserIdAndTaleId(Long userId, Long taleId);
//
//    void deleteByUserId(int userId);
}