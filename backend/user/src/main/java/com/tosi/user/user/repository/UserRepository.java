package com.tosi.user.user.repository;

import com.ssafy.tosi.user.dto.UserInfo;
import com.ssafy.tosi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    @Query("SELECT new com.ssafy.tosi.user.dto.UserInfoResponse(u.userId, u.email, u.bookshelfName, u.childrenList) FROM User u WHERE u.userId = :userId")
    UserInfo findUserInfoResponseByUserId(@Param("userId") Integer userId);

}