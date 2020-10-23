package com.hbr.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
import java.util.UUID;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/17 15:12
 */
public class FileUtil {

    /**
     * 普通文件转换成 MultipartFile
     * @param file
     * @return
     */
    public static MultipartFile fileToMultipart(File file) {

        FileInputStream inputStream;
        MultipartFile multipartFile = null;
        try {
            inputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(), inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipartFile;
    }


    /**
     * 文件转换成base64   不需要用到
     * @param path
     * @return
     */
    public String fileToBase64(String path) {
        String base64 = null;
        InputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            base64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }


    /**
     * 将base64转换图片文件，
     * @param base64Data
     * @return
     */
    public static String base64ToFile(String base64Data, String folderPath){

        String dataPrix = ""; //base64格式前头
        String data = "";//实体部分数据
        if(base64Data==null||"".equals(base64Data)){
            System.out.println("上传失败，上传图片数据为空");
//            return "上传图片数据为空";
            return "404";
        }else {
            String [] d = base64Data.split("base64,");//将字符串分成数组
            if(d != null && d.length == 2){
                dataPrix = d[0];
                data = d[1];
            }else {
                System.out.println("上传失败，数据不合法");
//                return "数据不合法";
                return "403";
            }
        }
        String suffix = "";//图片后缀，用以识别哪种格式数据
        //data:image/jpeg;base64,base64编码的jpeg图片数据
        if("data:image/jpeg;".equalsIgnoreCase(dataPrix)){
            suffix = ".jpg";
        }else if("data:image/x-icon;".equalsIgnoreCase(dataPrix)){
            //data:image/x-icon;base64,base64编码的icon图片数据
            suffix = ".ico";
        }else if("data:image/gif;".equalsIgnoreCase(dataPrix)){
            //data:image/gif;base64,base64编码的gif图片数据
            suffix = ".gif";
        }else if("data:image/png;".equalsIgnoreCase(dataPrix)){
            //data:image/png;base64,base64编码的png图片数据
            suffix = ".png";
        }else {
            System.out.println("上传图片格式不合法");
//            return "上传图片格式不合法";
            return "405";
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", ""); //产生唯一id
        String tempFileName = uuid+suffix; // 图片文件的名字
        String imgFilePath = folderPath + tempFileName;//新生成的图片路径

        try {
            //Base64解码
            byte[] b = Base64Utils.decodeFromString(data);
            for(int i=0;i<b.length;++i) {
                if(b[i]<0) {
                    //调整异常数据
                    b[i]+=256;
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
//            String imgurl="http://xxxxxxxx/"+tempFileName;
            //imageService.save(imgurl);
//            System.out.println(imgurl + "200");
            return imgFilePath;
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("上传图片失败"+"401");
            return "401";
        }

    }

    /**
     * 将文件上传到FastFDS服务器
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String uploadToFastFDS(String filePath, FastFileStorageClient storageClient, ThumbImageConfig thumbImageConfig) {
        File file = new File(filePath);

        // 获取图片文件后缀名，确定上传格式
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//        System.out.println(suffix);

        MultipartFile multipartFile = FileUtil.fileToMultipart(file);

        String thumbImagePath = "";
        try {

            StorePath storePath = storageClient.uploadImageAndCrtThumbImage(multipartFile.getInputStream(), multipartFile.getSize(), suffix, null);
            thumbImagePath = thumbImageConfig.getThumbImagePath(storePath.getFullPath());
//            System.out.println("上传图片的缩略图为：" + thumbImagePath);
        }catch (IOException e) {
            e.printStackTrace();
            return "err";
        }
        return thumbImagePath;
    }

}
