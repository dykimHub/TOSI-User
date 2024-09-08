package com.tosi.user.user.repository;

import com.ssafy.tosi.user.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Integer> {
    @Modifying
    @Query("delete from Child c where c.userId = :userId")
    void deleteByUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM Child c WHERE c.userId = :userId ORDER BY c.myBaby DESC, c.childName ASC")
    List<Child> findByUserIdOrderByMyBabyDescAndChildNameAsc(@Param("userId") Integer userId);
}
