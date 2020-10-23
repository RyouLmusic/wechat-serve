package com.hbr.service;

import com.hbr.pojo.ChatMsg;

import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/10/20 20:40
 */
public interface ChatMsgService {
    int saveMsg(ChatMsg chatMsg);

    List<ChatMsg> getChatRecord(String senderId, String accepterId);
}
