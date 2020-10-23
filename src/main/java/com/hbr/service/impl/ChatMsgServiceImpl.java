package com.hbr.service.impl;

import com.hbr.mapper.ChatMsgMapper;
import com.hbr.pojo.ChatMsg;
import com.hbr.service.ChatMsgService;
import com.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/10/20 20:40
 */
@Service
public class ChatMsgServiceImpl implements ChatMsgService {

    @Autowired
    ChatMsgMapper mapper;
    // msgId的生成
    Sid sid;
    public ChatMsgServiceImpl() {
        sid = new Sid();
    }

    @Override
    public int saveMsg(ChatMsg chatMsg) {
        // 生成消息表的id
        chatMsg.setId(sid.nextShort());
        // 插入消息的时间
        chatMsg.setCreateTime(new Date());

        int i = mapper.insertSelective(chatMsg);
        return i;
    }

    @Override
    public List<ChatMsg> getChatRecord(String senderId, String accepterId) {
        return mapper.queryByTowId(senderId, accepterId);
    }
}
