package atguigu.com.newsbeijing_library.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * Created by sun on 2017/6/7.
 */

public class BitmapCacheUtils {

    private NetCachUtils netCachUtils;
    //本地
    private LocalCachUtils localCachUtils;
    //内存
    private MemoryCachUtils memoryCachUtils;


    public BitmapCacheUtils(Handler handler) {
        memoryCachUtils=new MemoryCachUtils();
        localCachUtils=new LocalCachUtils(memoryCachUtils);
        netCachUtils=new NetCachUtils(handler,localCachUtils,memoryCachUtils);
    }

    public Bitmap getBitMap(String imageUrl, int position) {

        if(memoryCachUtils!=null){
          Bitmap bitmap=  memoryCachUtils.getBitMapFromMemory(imageUrl);
            if(bitmap!=null){
                Log.e("TAG", "图片是从内存获取的" + position);
                return bitmap;
            }
        }

        if(localCachUtils!=null){
           Bitmap bitmap= localCachUtils.getBitMap(imageUrl);
            if(bitmap!=null){
                Log.e("TAG", "图片是从本地获取的" + position);
                return bitmap;
            }
        }


        netCachUtils.getImagerFromNet(imageUrl,position);
        return null;
    }
}
