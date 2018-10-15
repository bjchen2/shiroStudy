package com.dao;

import com.vo.UserVO;

import java.util.List;

/**
 * Created By Cx On 2018/10/13 15:06
 */
public interface UserDao {
    UserVO getUserByUserName(String username);

    List<String> getRolesByUserName(String username);
}
