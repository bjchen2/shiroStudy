package com.dao.impl;

import com.dao.UserDao;
import com.vo.UserVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created By Cx On 2018/10/13 15:07
 */
@Component
public class UserDaoImpl implements UserDao {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public UserVO getUserByUserName(String username) {
        String sql = "select username,password from user where username = ?";
        List<UserVO> userVOS = jdbcTemplate.query(sql, new String[]{username}, (resultSet, i) -> {
            UserVO userVO = new UserVO();
            userVO.setPassword(resultSet.getString("password"));
            userVO.setUsername(resultSet.getString("username"));
            return userVO;
        });
        if (CollectionUtils.isEmpty(userVOS)) return null;
        return userVOS.get(0);
    }

    @Override
    public List<String> getRolesByUserName(String username) {
        String sql = "select role_name from user_role where username = ?";
        return jdbcTemplate.query(sql, new String[]{username}, (resultSet, i) -> resultSet.getString(0));
    }
}
