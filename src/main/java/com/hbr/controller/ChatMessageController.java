package com.hbr.controller;

import com.hbr.pojo.ChatMsg;
import com.hbr.service.ChatMsgService;
import com.hbr.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/10/21 20:26
 */
@RestController
public class ChatMessageController {

    @Autowired
    ChatMsgService chatMsgService;

    @GetMapping("/chatRecord")
    public ResponseData chatRecord (@RequestParam String senderId, @RequestParam String accepterId) {
        List<ChatMsg> chatRecords = chatMsgService.getChatRecord(senderId, accepterId);
        return ResponseData.ok();
    }


}
