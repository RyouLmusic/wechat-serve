package com.hbr.pojo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/28 14:37
 */
@Data
public class FriendsList {
    String initial;
    private List<MyFriends> list;
}
