package com.hrm.auth.mapper.ext;

import com.hrm.auth.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface ExtUserMapper {
    Optional<User> findByUsername(@Param("username") String userName);

    Optional<User> findById(@Param("id") UUID id);
}
