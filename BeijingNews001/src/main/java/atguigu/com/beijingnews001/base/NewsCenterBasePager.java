package atguigu.com.beijingnews001.base;

import android.content.Context;
import android.view.View;

/**
 * Created by sun on 2017/6/8.
 */

public abstract class NewsCenterBasePager {

    public final Context context;
    public final View rootview;

    public NewsCenterBasePager(Context context){
        this.context=context;
        rootview=initview();
    }

    public abstract View initview();

    public void initdata(){

    }


}
