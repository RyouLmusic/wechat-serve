package com.hbr.service;

import com.hbr.pojo.MyFriends;

import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/18 22:29
 */
public interface MyFriendsService {
    MyFriends isExistFriend(String myId, String id);

    MyFriends addFriend(String myUserId, String myFriendUserId);

    List<MyFriends> queryById(String myId);
}
