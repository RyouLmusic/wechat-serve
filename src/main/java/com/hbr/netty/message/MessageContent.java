package com.hbr.netty.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/10/19 22:14
 */
@Data
public class MessageContent implements Serializable {

    private Integer action;//动作类型

    private String senderId;//发送者id
    private String receiverId;//接收者id
    private String content;//聊天内容

    private String extend;//扩展字段
}
