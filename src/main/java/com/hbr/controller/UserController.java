package com.hbr.controller;

import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hbr.pojo.FriendsRequest;
import com.hbr.pojo.MyFriends;
import com.hbr.pojo.User;
import com.hbr.service.FriendsRequestService;
import com.hbr.service.MyFriendsService;
import com.hbr.service.UserService;
import com.hbr.utils.FileUtil;
import com.hbr.utils.MD5Utils;
import com.hbr.utils.QRCodeUtil;
import com.hbr.utils.ResponseData;
import com.n3r.idworker.Sid;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/13 12:30
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    MyFriendsService myFriendsService;
    @Autowired
    FriendsRequestService friendsRequestService;

    @Autowired
    protected FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    Sid sid;
    public UserController() {
        sid = new Sid();
    }


//    @RequestMapping("/user")
//    public List<User> getUser() {
//        return userService.getUser();
//    }


    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

// const user = await axios.get('http://127.0.0.1:8080/wechat/user/username/password')
// console.log(user)
//
//
//    @RequestMapping("/user/{username}/{password}")
//    public User queryByUsername(@PathVariable("username") String username, @PathVariable("password") String password) {
//        User user = userService.queryByUsername(username);
//        System.out.println(password);
//        return user;
//    }

    /**
     * 登录和注册前的查询，是否已经存在此用户
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public ResponseData queryByUsername(String username, String password) {
        User user = userService.queryByUsername(username);
        System.out.println(user);
        if (user == null) {
            return ResponseData.errorMsg("账号错误");

        } else if (!user.getPassword().equals(MD5Utils.getPwd(password))) { // TODO MD5Utils.getPwd(password)
            return ResponseData.errorMsg("密码错误");
        } else {
            return ResponseData.ok(user);
        }

    }

    /**
     * 注册接口
     * @param map
     * @return
     */
    @PostMapping("/register")
    public ResponseData register(@RequestBody(required=true) Map<String,Object> map ) {

        System.out.println("****************"+map.get("username"));
        System.out.println("****************"+map.get("password"));

        // 先进行查询，是否已经存在此用户
        User userByQuery = userService.queryByUsername(map.get("username").toString());
        System.out.println(userByQuery);
        if (userByQuery != null) {
            return ResponseData.errorMsg("账号已经存在");
        } else {
            // 用户不存在，进行增加用户

            // 新建一个User对象
            // 进行密码加密
            String pass = MD5Utils.getPwd(map.get("password").toString());
            User user = new User();
            // 生成Id
            String userId = sid.nextShort();
            user.setId(userId);
            user.setPassword(pass);
            user.setUsername(map.get("username").toString());

            // 二维码的生成
            String qrCodePath = "E:\\Images\\qrcode";
            String qrCodeFileName = "";
            try {
                qrCodeFileName = QRCodeUtil.createQRCode(qrCodePath, userId, 300, 300);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String qrCodeFile = qrCodePath + "\\" + qrCodeFileName;

            // 进行上传到FastFDS服务器
            String qrcodeFDS = FileUtil.uploadToFastFDS(qrCodeFile, storageClient, thumbImageConfig);

            String qrcode = "192.168.87.140/" + qrcodeFDS.replace("_150x150", "");
            //设置user属性，不能为空
            user.setFaceImage("");
            user.setFaceImageBig("");
            user.setNickname("");
            user.setQrcode(qrcode);
            // TODO 前端获取到这个设备id，再来设置
            user.setCid("");
            // 进行添加用户
            User u = userService.insert(user);
            return ResponseData.ok(u);
        }
    }

    /**
     * 用户信息更新接口
     * @param map
     * @return
     */
    @PutMapping("/update")
    public ResponseData updateNikName(@RequestBody Map<String,Object> map) {
        // 将前端传来的数据转换成可以获取的LinkedHashMap
        LinkedHashMap<String, Object> userMap = (LinkedHashMap<String, Object>)map.get("user");

//        System.out.println(userMap.get("newNickname"));
        // 在数据库里面查询到此用户
        User user = userService.getUser(userMap.get("id").toString());
        if (user == null) { //如果没有查询到此用户，就返回null
            return ResponseData.errorMsg("没有查询到你的账号,请重新登录");
        } else { // 查到了此用户，进行昵称修改， 或者其他操作
            // 设置昵称
            String newNickname = userMap.get("newNickname").toString();
            user.setNickname(newNickname);
            System.out.println(user);

            userService.updateNickname(user);
            return ResponseData.ok(user);
        }
    }

    /**
     * 选择头像
     * @param map
     * @return
     */
    @PostMapping("/selectAvatar")
    public ResponseData selectAvatar(@RequestBody Map<String,String> map) {

        // TODO 在此之前应该删除之前的头像

        // 获取到前端传来的数据
//        System.out.println(map.get("base64"));
        System.out.println(map.get("id"));
        System.out.println(map.get("imageName"));
        // 将base64数据转换成图片文件，并且存储在  "E:\Images\" 下
        String avatarPath = FileUtil.base64ToFile(map.get("base64"), "E:\\Images\\");
//        System.out.println(avatarPath);

        // 判断存储图片到本地是否有错误
        switch (avatarPath) {
            case "404":
                return ResponseData.errorMsg("上传图片数据为空");
            case "403":
                return ResponseData.errorMsg("图片数据不合法");
            case "405":
                return ResponseData.errorMsg("上传图片格式不合法");
            case "401":
                return ResponseData.errorMsg("上传图片失败");
        }



        //上传到FastFDS服务器
        final val s = FileUtil.uploadToFastFDS(avatarPath, storageClient, thumbImageConfig);
        // 判断是否出错
        if (s.equals("err")) {
            return ResponseData.errorMsg("上传失败");
        }
        String smallAccessPath = "192.168.87.140/" + s;
        String bigAccessPath = "192.168.87.140/" + s.replace("_150x150", "");
        System.out.println(smallAccessPath);
        System.out.println(bigAccessPath);
        // 将数据库里面的头像数据进行修改
        // new一个user进行update
        User user = new User();
        user.setId(map.get("id"));
        user.setFaceImage(smallAccessPath);
        user.setFaceImageBig(bigAccessPath);

        User u = userService.updateFaceImage(user);
        return new ResponseData(200, "上传成功", u);
    }

    /**
     * 查询用户，然后再决定是否可以添加，或者进行聊天等操作
     * @param myId
     * @param friendUsername
     * @return
     */
    @RequestMapping("/searchFriend")
    public ResponseData searchFriend(String myId, String friendUsername) {
        System.out.println(myId);
        System.out.println(friendUsername);
        // 先对想要添加的好友进行查询，看是否存在，而且是否是自己本身
        // msg:404 :没有此用户
        // msg:500 :是本人
        // msg:200 :已经是好友   跟是本人的反应应该一致
        // msg:402 :可以添加好友到通讯录

        // 进行查询
        User user = userService.queryByUsername(friendUsername);

        if (user == null) {
            // 没有此用户
            return ResponseData.errorMsg("404");
        } else if (user.getId().equals(myId)) {
            // 是本人
            return new ResponseData(200, "500", user);
        } else {
            // 查询是否是好友
            MyFriends myFriend = myFriendsService.isExistFriend(myId, user.getId());
            System.out.println(myFriend);

            // 看这个数据是否为空，如果为空，就不是自己的好友，否则已经就是自己的好友
            if (myFriend != null) {
                // 是自己的好友， 可以进行聊天操作
                return new ResponseData(200, "200", user);
            } else {
                // 不是自己的好友，可以进行添加操作
                return new ResponseData(200, "402", user);
            }
        }

    }

    /**
     * 添加好友的请求，进行friendsRequest表数据的添加
     * @param map
     * @return
     */
    @PostMapping("/addFriendRequest")
    public ResponseData addFriendRequest(@RequestBody Map<String, String> map) {
//        System.out.println(map.get("sendId"));
//        System.out.println(map.get("acceptId"));
        String sentId = map.get("sendId");
        String acceptId = map.get("acceptId");
        // 从前端传来的数据为空 ，直接返回错误
        if (sentId.equals("") || acceptId.equals("")) {
            return ResponseData.errorMsg("Id为空");
        }
        // 查看前端发来是数据是否是假数据 ，就是接收端是否存在此用户
        User acceptUser = userService.getUser(acceptId);
        // 没有此用户，直接返回信息
        if (acceptUser == null) {
            return ResponseData.errorMsg("没有此用户");
        }
        // 查询是否已经添加过，添加过的话，不需要再次进行添加到数据库里面 直接进行返回就可以
        FriendsRequest request = friendsRequestService.selectByTwoId(sentId, acceptId);
        if (request != null) {
            return ResponseData.ok(request);
        }
        // 此时可以进行添加用户请求数据
        FriendsRequest friendsRequest = friendsRequestService.insert(sentId, acceptId);
        return ResponseData.ok(friendsRequest);
    }

    /**
     * 此处用连表查询会更加好，减少请求次数， 连接user表 ,, 以后改进---直接返回发出请求的user信息，左右join
     * @param id
     * @return
     */
    @GetMapping("/getRequestList/{id}")
    public ResponseData getRequestList(@PathVariable("id") String id) {

        List<FriendsRequest> requests = friendsRequestService.selectByAcceptId(id);

//        System.out.println(requests.toArray());
        for (FriendsRequest request : requests) {
//            System.out.println(request);
        }
        return ResponseData.ok(requests);
    }

    /**
     * 拒绝好友请求之后  进行删除friendsRequest表里面的内容
     * @param id
     * @return
     */
    @DeleteMapping("deleteRequest/{id}")
    public ResponseData deleteRequest(@PathVariable("id") String id) {
        // 直接进行删除
        friendsRequestService.deleteById(id);

        // 进行查询删除之后的
        return ResponseData.ok();
    }

}