package atguigu.com.newsbeijing_library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by sun on 2017/6/7.
 */

public class LocalCachUtils {

    private final MemoryCachUtils memoryCachUtils;

    public LocalCachUtils(MemoryCachUtils memoryCachUtils) {
        this.memoryCachUtils=memoryCachUtils;
    }

    public void putImagerToLocal(String imageUrl, Bitmap bitmap) {


        try {
            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/";
            String fileName = MD5Encoder.encode(imageUrl);

            File file = new File(dir, fileName);

            File parentFile = file.getParentFile();

            if(!parentFile.exists()){
                parentFile.mkdirs();
            }

            if(!file.exists()){
                file.createNewFile();
            }

            FileOutputStream fos=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Bitmap getBitMap(String imageUrl) {
        try {
            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/";
            String fileName = MD5Encoder.encode(imageUrl);

            File file = new File(dir, fileName);
            if(file.exists()){
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

                if(bitmap!=null){
                    memoryCachUtils.putBitMapToMemory(imageUrl,bitmap);
                }

                return  bitmap;
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
