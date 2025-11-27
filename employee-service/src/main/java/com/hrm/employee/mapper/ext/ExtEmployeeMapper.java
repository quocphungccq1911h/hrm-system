package com.hrm.employee.mapper.ext;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

@Mapper
public interface ExtEmployeeMapper {
    // DELETE (Deactivate)
    void deactivate(@Param("id") UUID id);
}
