package com.tosi.user.common.redis.repository;

import com.tosi.user.common.redis.entity.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;


public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
