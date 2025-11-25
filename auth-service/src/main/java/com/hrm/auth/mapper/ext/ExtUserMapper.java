package com.hrm.auth.mapper.ext;

import com.hrm.auth.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ExtUserMapper {
    Optional<User> findByUsername(String userName);
}
