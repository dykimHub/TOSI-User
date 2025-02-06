package com.tosi.user.redis.repository;

import com.tosi.user.redis.entity.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;


public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
