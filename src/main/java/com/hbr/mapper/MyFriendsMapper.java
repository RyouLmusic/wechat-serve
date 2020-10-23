package com.hbr.mapper;

import com.hbr.pojo.MyFriends;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyFriendsMapper {
    int deleteByPrimaryKey(String id);

    int insert(MyFriends record);

    int insertSelective(MyFriends record);

    MyFriends selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MyFriends record);

    int updateByPrimaryKey(MyFriends record);

    MyFriends queryByFriendId(String myId, String fid);

    List<MyFriends> selectByMyUserId(String myId);
}