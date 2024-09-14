package com.tosi.user.repository;

import com.tosi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nick);

//    @Query("SELECT new com.ssafy.tosi.user.dto.UserInfoResponse(u.userId, u.email, u.bookshelfName, u.childrenList) FROM User u WHERE u.userId = :userId")
//    UserInfo findUserInfoResponseByUserId(@Param("userId") Integer userId);

}