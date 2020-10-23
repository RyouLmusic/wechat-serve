package com.hbr.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class FriendsRequest {
    private String id;

    private String sendUserId;

    private String acceptUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date requestDateTime;

    private User sentUser;

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

    public Date getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(Date requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public User getSentUser() {
        return sentUser;
    }

    public void setSentUser(User sentUser) {
        this.sentUser = sentUser;
    }

    @Override
    public String toString() {
        return "FriendsRequest{" +
                "id='" + id + '\'' +
                ", sendUserId='" + sendUserId + '\'' +
                ", acceptUserId='" + acceptUserId + '\'' +
                ", requestDateTime=" + requestDateTime +
                ", userList=" + sentUser +
                '}';
    }
}