package com.moaike.assetpngen.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 2017/2/16.
 */

public class AssetsImageUtils {
    private static Context context;
    private static AssetManager assets;
    private static String[] pictures; //asstes/picture 下文件


    public  AssetsImageUtils(Context context){
        this.context = context;
        this.assets = context.getAssets();
        try{
            pictures = assets.list("picture");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * 将assets/picture 下的文件写入到 /data/data/../file下
     * */
    public  void imgeFromAssetsToFile(){
        /*for(int i = 0;i< pictures.length;i++){
            Bitmap bitmap = readBitmap(context, "picture/"+pictures[i]);   //获取解密后的图片
            writeFileData(pictures[i],Bitmap2Bytes(bitmap)); //文件写入到/data/data/../file
        }*/
       // Bitmap ss =  getPicFromBytes(readFileData(pictures[currentPicture]),null);//读取 /data/data/../file下文件
        //更加可能加载图片的先后属性加载图片
        String[] picture = new String[]{"second.dat","ad_head.dat","ad_head_duandai.dat","ad_svip_head.dat"};

        for(int i = 0;i< picture.length;i++){
            Bitmap bitmap = readBitmap(context, "picture/"+picture[i]);   //获取解密后的图片
            writeFileData(picture[i],Bitmap2Bytes(bitmap)); //文件写入到/data/data/../file
        }
    }
    public void imageFromFirst(){
        Bitmap bitmap = readBitmap(context, "picture/splash.dat");   //获取解密后的图片
        writeFileData("splash.dat",Bitmap2Bytes(bitmap)); //文件写入到/data/data/../file
    } 
    public static Bitmap readFileByName(String fileName){
        Bitmap ss =  getPicFromBytes(readFileData(fileName),null);//读取 /data/data/../file下文件
        return ss;
    }
    public static Bitmap readAssetsByName(String filename){
        Bitmap ss = readBitmap(context, "picture/"+filename);
        return ss;
    }
    public static int getImageHeight(Context c){
        WindowManager wm = (WindowManager) c
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int currentWidth = width - DensityUtil.dip2px(c,40);
        int curentHeight = (currentWidth*727)/498;

        return curentHeight;
    }

    /**
     * 异或解密图片方法
     * @param context
     * @param fileName
     * @return
     */
    private   static Bitmap readBitmap(Context context, String fileName) {
        Bitmap bitmap = null;
        List<Byte> list = new ArrayList<Byte>();
        try {
            InputStream is = context.getAssets().open(fileName);
            int read;
            while ((read = is.read()) > -1) {
                read = read ^ 0X99;     // 密钥
                list.add((byte) read);
            }
            byte[] arr = new byte[list.size()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (Byte) list.get(i);    //list转成byte[]
            }
            //通过byte数组获取二进制图片流
            bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
            System.out.println(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private static Bitmap getPicFromBytes(byte[] bytes,
                                          BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }
    private static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    private static void writeFileData(String fileName, byte[] bytes){
        try{
            String path = context.getFilesDir().getAbsolutePath() ;
            File file = new File(path +"/"+fileName) ;
        /*    Log.i("HACK-TAG","file " +file);
            Log.i("HACK-TAG","file.exists() " +file.exists());*/
            if(!file.exists()){
                FileOutputStream fout =context.openFileOutput(fileName, MODE_PRIVATE);
                fout.write(bytes);
                fout.close();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    private static byte[] readFileData(String fileName){

        byte[] buffer= null;

        try{
            String path = context.getFilesDir().getAbsolutePath() ;
           /* File file = new File(path +"/"+fileName) ;
            Log.i("HACK-TAG","readFileData " +file);
            Log.i("HACK-TAG","readFileData   exists() " +file.exists());*/
            FileInputStream fin = context.openFileInput(fileName);
            int length = fin.available();
            buffer = new byte[length];
            fin.read(buffer);
            fin.close();
        }
        catch(Exception e){

            e.printStackTrace();

        }

        return buffer ;

    }
}
