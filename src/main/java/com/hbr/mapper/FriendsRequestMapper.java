package com.hbr.mapper;

import com.hbr.pojo.FriendsRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsRequestMapper {
    int deleteByPrimaryKey(String id);

    int insert(FriendsRequest record);

    int insertSelective(FriendsRequest record);

    FriendsRequest selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FriendsRequest record);

    int updateByPrimaryKey(FriendsRequest record);

    FriendsRequest selectByTwo(String sentId, String acceptId);

    List<FriendsRequest> selectByAcceptId(String id);
}