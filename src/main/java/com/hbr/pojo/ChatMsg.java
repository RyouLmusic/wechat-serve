package com.hbr.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ChatMsg {
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String msg;

    private Integer signFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    // 1为展示， 0为不展示
    private Integer inList;

    public ChatMsg(String sendUserId, String acceptUserId, String msg, Integer signFlag, Integer inList) {
        this.sendUserId = sendUserId;
        this.acceptUserId = acceptUserId;
        this.msg = msg;
        this.signFlag = signFlag;
        this.inList = inList;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}