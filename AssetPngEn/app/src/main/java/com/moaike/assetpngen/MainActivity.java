package com.moaike.assetpngen;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.moaike.assetpngen.utils.EncryptDemo;
import com.moaike.assetpngen.utils.ImageDispose;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView show_picture_img;
    private Button nextBtn;
    private String[] pictures;
    private int currentPicture = 0;
    private AssetManager assets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadAsstesData();
        initView();
    }

    private void initView() {
        show_picture_img = (ImageView) findViewById(R.id.show_picture_img);

        nextBtn = (Button) findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);

     /*   Bitmap bitmap = readBitmap(this, "img/ad_head.dat");   //获取解密后的图片
        show_picture_img.setImageBitmap(bitmap);
*/
    }
    private void loadAsstesData(){
        assets = getResources().getAssets();
        try{
            pictures = assets.list("img");

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * 异或解密图片方法
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap readBitmap(Context context, String fileName) {
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
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.next_btn){
            //
            if(currentPicture>=pictures.length){
                currentPicture = 0;
            }
            Bitmap bitmap = readBitmap(this, "img/"+pictures[currentPicture]);   //获取解密后的图片


            writeFileData(pictures[currentPicture],Bitmap2Bytes(bitmap)); //文件写入到/data/data/../file
            // readAssetsPictureImage2Show();
            Bitmap ss =  getPicFromBytes(readFileData(pictures[currentPicture]),null);//读取 /data/data/../file下文件
            show_picture_img.setImageBitmap(ss);
            currentPicture++;
        }
    }
    private void readAssetsPictureImage2Show(){
             InputStream inputStream = null;
            Log.i("HACK-TAG","picture name    "+pictures[currentPicture]);
            try{
                inputStream = assets.open("picture/"+pictures[currentPicture]);

               // String filePath ="a.txt";
                //imageToFlie(inputStream,filePath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                show_picture_img.setImageBitmap(bitmap);
                currentPicture++;
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(inputStream != null){
                    try{
                        inputStream.close();
                        inputStream = null;
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }
    }
    public static Bitmap getPicFromBytes(byte[] bytes,
                                         BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }
    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public void writeFileData(String fileName,byte[] bytes){
        try{
            FileOutputStream fout =openFileOutput(fileName, MODE_PRIVATE);
            fout.write(bytes);
            fout.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public byte[] readFileData(String fileName){

        byte[] buffer= null;

        try{

            FileInputStream fin = openFileInput(fileName);

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
    private void imageToFlie(  InputStream inputStream ,String name ){
        //图片转换成Byte[]
        //将Byte[]保持到文件中

        try {
            byte[] img = ImageDispose.readStream(inputStream);

            ImageDispose.getFileFromBytes(img,name);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
