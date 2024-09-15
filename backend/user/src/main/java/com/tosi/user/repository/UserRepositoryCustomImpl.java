package com.tosi.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tosi.user.dto.QUserDto;
import com.tosi.user.dto.UserDto;
import com.tosi.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserDto> findUserDtoByEmail(String email) {
        QUser qUser = QUser.user;
        return Optional.ofNullable(queryFactory.select(new QUserDto(
                        qUser.userId,
                        qUser.email,
                        qUser.nickname,
                        qUser.bookshelfName))
                .from(qUser)
                .where(qUser.email.eq(email))
                .fetchOne()
        );
    }
}
