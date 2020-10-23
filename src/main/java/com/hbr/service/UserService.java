package com.hbr.service;

import com.hbr.pojo.User;

import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/13 12:28
 */
public interface UserService {
    User getUser(String id);
    List<User> getUser();

    User queryByUsername(String username);

    User insert(User user);

    User updateNickname(User user);

    User updateFaceImage(User user);
}
