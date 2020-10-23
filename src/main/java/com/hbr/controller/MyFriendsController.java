package com.hbr.controller;

import com.hbr.pojo.FriendsList;
import com.hbr.pojo.MyFriends;
import com.hbr.pojo.User;
import com.hbr.service.MyFriendsService;
import com.hbr.service.UserService;
import com.hbr.utils.PinYinUtil;
import com.hbr.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/22 21:44
 */
@RestController
public class MyFriendsController {

    @Autowired
    MyFriendsService myFriendsService;
    @Autowired
    UserService userService;

    /**
     * 同意添加好友之后进行my_friends表的数据增加  // TODO。一个URL就可以
     * @param map
     * @return
     */
    @PostMapping("/addFriend")
    public ResponseData addFriend (@RequestBody Map<String, String> map) {
//        System.out.println(map.get("myUserId"));
//        System.out.println(map.get("myFriendUserId"));
        String myUserId = map.get("myUserId");
        String myFriendUserId = map.get("myFriendUserId");
        if (myUserId == null || myFriendUserId == null) {
            return ResponseData.errorMsg("没有传来数据");
        }
        // 进行表增加,相互加为好友
        MyFriends myFriend = myFriendsService.addFriend(myUserId, myFriendUserId);
        myFriendsService.addFriend(myFriendUserId, myUserId);

        User user = userService.getUser(myFriend.getMyFriendUserId());
        // 对好友数据进行返回
        return ResponseData.ok(user);
    }

    @GetMapping("/queryAllFriends")
    public ResponseData queryAllFriends(@RequestParam String myId) {

        List<MyFriends> myFriends = myFriendsService.queryById(myId);


        // 放置通讯录， index是A，B，C等 ,可以传回前端的数据
        Map<String, FriendsList> map = new TreeMap<>();

//        循环放入好友
        for (MyFriends friend: myFriends) {
            // 获取首字母
            String index = PinYinUtil.getPingYin(friend.getFriend().getNickname());
//            根据首字母进行放入
            if (map.get(index) == null) {  // 如果是空的话,进行new
                FriendsList friendsList = new FriendsList();
                friendsList.setInitial(index);

                // 放friend
                List<MyFriends> list = new ArrayList<>();
                friendsList.setList(list);
                map.put(index, friendsList);
            }
            map.get(index).getList().add(friend);
        }
//        System.out.println(myFriends);
        return ResponseData.ok(map.values());
    }
}
