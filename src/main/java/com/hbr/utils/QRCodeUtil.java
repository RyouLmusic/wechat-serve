package com.hbr.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/19 11:58
 */
public class QRCodeUtil {

    /**
     * 生成二维码
     * @param filePath 需要存储到的位置
     * @param content 二维码中包含的内容
     * @param width 二维码的宽度
     * @param height 二维码的高度
     * @return fileName 返回文件的随机名称
     */
    public static String createQRCode(String filePath,String content,int width,int height) throws Exception{

        //如果文件夹不存在就创建文件夹
        File temp = new File(filePath);
        if (!temp.exists()){
            temp.mkdirs();
        }

        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置字符的编码
        hints.put(EncodeHintType.MARGIN, 1);//设置外边距的距离
        BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(Color.BLACK.getRGB(), Color.WHITE.getRGB());

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix,matrixToImageConfig);

        String fileName = UUID.randomUUID() + ".jpg";//随机生成二维码的名称
        ImageIO.write(bufferedImage, "jpg", new File(filePath + File.separator + fileName));//将图片写到磁盘

        return fileName;
    }

    /**
     * 解析二维码
     * @param path 文件的路径
     * @return URL
     */
    public String analysisQRCode(String path) throws Exception {

        File file = new File(path);
        if (!file.isFile()) return null;//判断该文件是否存在

        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(file))));

        HashMap hints = new HashMap();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");//设置字符集

        //获取二维码的内容
        String result = new QRCodeReader().decode(bitmap, hints).getText();

        return result;
    }
}
