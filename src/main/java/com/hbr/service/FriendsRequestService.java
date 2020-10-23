package com.hbr.service;

import com.hbr.pojo.FriendsRequest;

import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/20 11:38
 */
public interface FriendsRequestService {
    FriendsRequest insert(String sentId, String acceptId);

    FriendsRequest selectByTwoId(String sentId, String acceptId);

    List<FriendsRequest> selectByAcceptId(String id);

    int deleteById(String id);
}
