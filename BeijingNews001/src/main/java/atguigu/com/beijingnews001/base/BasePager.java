package atguigu.com.beijingnews001.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import atguigu.com.beijingnews001.MainActivity;
import atguigu.com.beijingnews001.R;

/**
 * Created by sun on 2017/6/8.
 */

public class BasePager {
    public final Context context;
    public final View rootview;

    public TextView tv_title;
    public ImageButton ib_menu;

    public FrameLayout fl_content;
    public ImageButton ib_list_glid;


    public BasePager(final Context context) {
        this.context = context;
        rootview = View.inflate(context, R.layout.base_pager, null);
        tv_title = (TextView) rootview.findViewById(R.id.tv_title);
        ib_menu= (ImageButton) rootview.findViewById(R.id.ib_menu);
        fl_content=(FrameLayout) rootview.findViewById(R.id.fl_content);
        ib_list_glid = (ImageButton) rootview.findViewById(R.id.ib_list_glid);

       ib_menu.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ((MainActivity) context).getSlidingMenu().toggle();
           }
       });
    }

    public void initData(){

    }

}
