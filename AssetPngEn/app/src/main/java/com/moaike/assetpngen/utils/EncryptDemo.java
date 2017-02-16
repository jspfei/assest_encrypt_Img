package com.moaike.assetpngen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by admin on 2017/2/16.
 */

public class EncryptDemo {

    public static final int XOR_CONST = 0X99; // 异或密钥

    /**
     * @param args
     */
    public static void main(String[] args) {

        File load = new File("E:\\file\\ad_head.jpg");      //原图
        File result = new File("E:\\file\\eee.dat");    //加密后得到的文件
        File reload = new File("E:\\file\\eee2.jpg");   //解密后得到的原图

        try {
            encryptImg(load, result);   // 加密原图
            encryptImg(result, reload); // 对加密后的文件再加密得到原图
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 文件流异或加密
     * @param src
     * @param dest
     * @throws Exception
     */
    public static void encryptImg(File load, File result) throws Exception {
        FileInputStream fis = new FileInputStream(load);
        FileOutputStream fos = new FileOutputStream(result);

        int read;
        while ((read = fis.read()) > -1) {
            fos.write(read ^ XOR_CONST);    //进行异或运算并写入结果
        }
        fos.flush();
        fos.close();
        fis.close();
    }
}
