package atguigu.com.newsbeijing_library.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by sun on 2017/6/7.
 */

public class MemoryCachUtils {

    private LruCache<String, Bitmap> lruCache;

    public MemoryCachUtils(){
        //这个是得到系统分配给当前应用的8分之一空间，用于存储图片
        int maxSize= (int) (Runtime.getRuntime().maxMemory()/8);
        lruCache=new LruCache<String , Bitmap>(maxSize){
            //计算每张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    public void putBitMapToMemory(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl,bitmap);
    }
    public Bitmap getBitMapFromMemory(String imageUrl) {
        return lruCache.get(imageUrl);
    }


}
