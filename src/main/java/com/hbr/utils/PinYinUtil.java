package com.hbr.utils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import lombok.val;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/27 22:42
 */
public class PinYinUtil {

    public static void main(String[] args) {
        String str = "你好世界";
        try {
            final val s = PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_MARK);// nǐ,hǎo,shì,jiè
            String a = PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_NUMBER); // ni3,hao3,shi4,jie4
            String b = PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITHOUT_TONE); // ni,hao,shi,jie
            String c = PinyinHelper.getShortPinyin(str); // nhsj
//            PinyinHelper.addPinyinDict("user.dict");  // 添加用户自定义字典


            System.out.println(s);
            System.out.println(a);
            System.out.println(b);
            System.out.println(c);


            System.out.println(getPingYin("y"));


        } catch (PinyinException e) {
            e.printStackTrace();
        }
    }

    public static String getPingYin(String str) {
        String result = "";
        try {
            String temp = PinyinHelper.getShortPinyin(str); // 获取拼音的字母
            result = String.valueOf(temp.charAt(0)).toUpperCase();  // 获取首字母，并且转换成大写
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return result;
    }

}
