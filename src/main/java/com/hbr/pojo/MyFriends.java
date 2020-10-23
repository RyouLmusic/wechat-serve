package com.hbr.pojo;

import java.util.List;

public class MyFriends {
    private String id;

    private String myUserId;

    private String myFriendUserId;

    private User friend;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    public String getMyFriendUserId() {
        return myFriendUserId;
    }

    public void setMyFriendUserId(String myFriendUserId) {
        this.myFriendUserId = myFriendUserId;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriends(User friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "MyFriends{" +
                "id='" + id + '\'' +
                ", myUserId='" + myUserId + '\'' +
                ", myFriendUserId='" + myFriendUserId + '\'' +
                ", friend=" + friend +
                '}';
    }
}