package com.tosi.user.JWT.repository;

import com.tosi.user.JWT.entity.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;


public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
