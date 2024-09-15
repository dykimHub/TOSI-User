package com.tosi.user.repository;

import com.tosi.user.dto.UserDto;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<UserDto> findUserDtoByEmail(String email);
}
