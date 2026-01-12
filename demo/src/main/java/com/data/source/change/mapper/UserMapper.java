package com.data.source.change.mapper;


import com.data.source.change.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {


    Integer add(UserEntity entity);

    List<UserEntity> listAll();

    Integer selectTest(@Param("ts") float ts);


}
