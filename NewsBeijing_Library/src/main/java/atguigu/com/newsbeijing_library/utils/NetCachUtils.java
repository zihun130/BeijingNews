package atguigu.com.newsbeijing_library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sun on 2017/6/7.
 */

public class NetCachUtils {
    //成功
    public static final int SUCESS = 1;
    //失败
    public static final int FAIL = 2;
    private final Handler handler;
    private final LocalCachUtils localCachUtils;
    private final ExecutorService executorService;
    private final MemoryCachUtils memoryCachUtils;

    public NetCachUtils(Handler handler, LocalCachUtils localCachUtils, MemoryCachUtils memoryCachUtils) {
        this.handler=handler;
        this.localCachUtils=localCachUtils;
        this.memoryCachUtils=memoryCachUtils;
        executorService = Executors.newFixedThreadPool(10);
    }

    public void getImagerFromNet(String imageUrl, int position) {
        //开启子线程
         //new Thread(new MyRunable(imageUrl,position)).start();
        executorService.execute(new MyRunable(imageUrl,position));
    }

    private class MyRunable implements Runnable {
        private final String imageUrl;
        private final int position;

        public MyRunable(String imageUrl, int position) {
            this.imageUrl=imageUrl;
            this.position=position;
        }

        @Override
        public void run() {

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(imageUrl).openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(5000);

                int responseCode = urlConnection.getResponseCode();
                if(200==responseCode){
                    //请求图片成功
                    InputStream inputStream = urlConnection.getInputStream();
                    //把流转换成bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    //发送到主线程
                    Message msg=Message.obtain();
                    msg.obj=bitmap;
                    msg.what=SUCESS;
                    msg.arg1=position;
                    handler.sendMessage(msg);
                    //内存保存
                    memoryCachUtils.putBitMapToMemory(imageUrl,bitmap);
                    //本地保存
                    localCachUtils.putImagerToLocal(imageUrl,bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Message msg=Message.obtain();
                msg.what=FAIL;
                msg.arg1=position;
                handler.sendMessage(msg);
            }

        }
    }
}
