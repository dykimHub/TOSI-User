package com.tosi.user.JWT.repository;

import com.tosi.user.JWT.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
