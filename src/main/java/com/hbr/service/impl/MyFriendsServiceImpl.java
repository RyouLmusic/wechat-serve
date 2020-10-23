package com.hbr.service.impl;

import com.hbr.mapper.MyFriendsMapper;
import com.hbr.pojo.MyFriends;
import com.hbr.service.MyFriendsService;
import com.n3r.idworker.Sid;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/18 22:29
 */
@Service
public class MyFriendsServiceImpl implements MyFriendsService {

    @Autowired
    MyFriendsMapper mapper;
    Sid sid;
    public MyFriendsServiceImpl() {
        sid = new Sid();
    }

    @Override
    public MyFriends isExistFriend(String myId, String fid) {
        return mapper.queryByFriendId(myId, fid);
    }

    @Override
    public MyFriends addFriend(String myUserId, String myFriendUserId) {
        // 设置属性
        MyFriends myFriends = new MyFriends();
        myFriends.setMyUserId(myUserId);
        myFriends.setMyFriendUserId(myFriendUserId);
        // 设置id
        String uuid = sid.nextShort();
        myFriends.setId(uuid);
        // 进行插入
        int i = mapper.insertSelective(myFriends);
        return myFriends;
    }

    @Override
    public List<MyFriends> queryById(String myId) {

        return mapper.selectByMyUserId(myId);
    }
}
