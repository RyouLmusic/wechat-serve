package com.hbr.service.impl;

import com.hbr.mapper.UserMapper;
import com.hbr.pojo.User;
import com.hbr.service.UserService;
import com.hbr.utils.MD5Utils;
import com.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/13 12:28
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper mapper;

    @Override
    public User getUser(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> getUser() {
        return mapper.selectAll();
    }

    @Override
    public User queryByUsername(String username) {
        User user = mapper.queryByUsername(username);
        return user;
    }

    @Override
    public User insert(User user) {
        mapper.insert(user);
        return user;
    }

    @Override
    public User updateNickname(User user) {
        mapper.updateByPrimaryKeySelective(user);
        return user;
    }

    @Override
    public User updateFaceImage(User user) {
        // 进行修改，然后进行查询
        mapper.updateByPrimaryKeySelective(user);

        User u = mapper.selectByPrimaryKey(user.getId());
        return u;
    }
}
