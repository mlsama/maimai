package com.maimai.sso.mapper;

import com.maimai.sso.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 描述:用户mapper,数据库访问接口.
 *
 * @Author:mlsama 2018/1/24 22:43
 */
@Mapper
public interface UserMapper {
    int countData(User user);

    void addUser(User user);

    User login(User user);
}
