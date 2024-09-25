package com.tosi.user.repository;

import com.tosi.user.dto.ChildDto;
import com.tosi.user.dto.UserDto;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<UserDto> findUserDtoById(Long userId);

    Optional<List<ChildDto>> findChildrenDtoById(Long userId);

    @Modifying
    Long modifyUser(UserDto userDto);
}
