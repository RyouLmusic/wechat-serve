package com.hbr.service.impl;

import com.hbr.mapper.FriendsRequestMapper;
import com.hbr.pojo.FriendsRequest;
import com.hbr.service.FriendsRequestService;
import com.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/20 11:38
 */
@Service
public class FriendsRequestServiceImpl implements FriendsRequestService {

    @Autowired
    FriendsRequestMapper mapper;

    Sid sid;
    public FriendsRequestServiceImpl () {
        sid = new Sid();
    }

    @Override
    public FriendsRequest insert(String sentId, String acceptId) {

        FriendsRequest request = new FriendsRequest();
        String requestId = sid.nextShort(); //获取唯一id
        // 进行属性设置
        request.setId(requestId);
        request.setSendUserId(sentId);
        request.setAcceptUserId(acceptId);
        request.setRequestDateTime(new Date());

        int i = mapper.insertSelective(request);

        return request;
    }

    @Override
    public FriendsRequest selectByTwoId(String sentId, String acceptId) {
        return mapper.selectByTwo(sentId, acceptId);
    }

    @Override
    public List<FriendsRequest> selectByAcceptId(String id) {
        return mapper.selectByAcceptId(id);
    }

    @Override
    public int deleteById(String id) {
        return mapper.deleteByPrimaryKey(id);
    }
}
