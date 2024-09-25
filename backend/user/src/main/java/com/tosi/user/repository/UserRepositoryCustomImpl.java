package com.tosi.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tosi.user.dto.ChildDto;
import com.tosi.user.dto.QChildDto;
import com.tosi.user.dto.QUserDto;
import com.tosi.user.dto.UserDto;
import com.tosi.user.entity.QChild;
import com.tosi.user.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * 회원 데이터베이스에서 회원 번호가 일치하는 회원을 찾고 Optional로 감싼 UserDto 객체로 변환하여 반환합니다.
     *
     * @param userId 로그인한 회원의 회원 번호
     * @return UserDto 객체
     */
    @Override
    public Optional<UserDto> findUserDtoById(Long userId) {
        QUser qUser = QUser.user;
        return Optional.ofNullable(queryFactory.select(new QUserDto(
                        qUser.userId,
                        qUser.email,
                        qUser.nickname,
                        qUser.bookshelfName))
                .from(qUser)
                .where(qUser.userId.eq(userId))
                .fetchOne()
        );
    }

    /**
     * 회원의 자녀 데이터베이스에서 회원 번호가 일치하는 자녀들을 찾아서 Optional로 감싼 ChildDto 객체 리스트로 변환하여 반환합니다.
     *
     * @param userId 회원 번호
     * @return ChildDto 객체 리스트
     */
    @Override
    public Optional<List<ChildDto>> findChildrenDtoById(Long userId) {
        QChild qChild = QChild.child;
        return Optional.ofNullable(queryFactory.select(new QChildDto(
                        qChild.childId,
                        qChild.childName,
                        qChild.childGender))
                .from(qChild)
                .where(qChild.userId.eq(userId))
                .fetch()
        );
    }

    /**
     * 회원 데이터베이스에서 아이디가 일치하는 회원을 찾고 닉네임 혹은 책장 이름을 업데이트 합니다.
     *
     * @param modifyingUserDto 수정할 정보가 담긴 UserDto 객체
     * @return 데이터베이스가 성공적으로 수정되면 1L, 수정된 항목이 없으면 0L을 반환
     */
    @Override
    public Long modifyUser(UserDto modifyingUserDto) {
        QUser qUser = QUser.user;
        return queryFactory.update(qUser)
                .set(qUser.nickname, modifyingUserDto.getNickname())
                .set(qUser.bookshelfName, modifyingUserDto.getBookshelfName())
                .where(qUser.userId.eq(modifyingUserDto.getUserId()))
                .execute();
    }


}
