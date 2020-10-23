package com.hbr.mapper;

import com.hbr.pojo.ChatMsg;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface ChatMsgMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChatMsg record);

    int insertSelective(ChatMsg record);

    ChatMsg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChatMsg record);

    int updateByPrimaryKey(ChatMsg record);

    List<ChatMsg> queryByTowId(String senderId, String accepterId);
}