package atguigu.com.beijingnews.Base;

import android.content.Context;
import android.view.View;

/**
 * Created by sun on 2017/6/3.
 */

public abstract class NewsCenterMenuBasePager {

    public View rootView;
    public Context context;
    public  NewsCenterMenuBasePager(Context context){
        this.context=context;
        rootView= initView();
    }

    public abstract View initView();

    public void initData(){

   };
}
