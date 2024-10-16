package com.tosi.user.repository;

import com.tosi.user.dto.TaleDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaleDtoRedisRepository extends CrudRepository<TaleDto, String> {
}
