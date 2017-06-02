package atguigu.com.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import atguigu.com.beijingnews.Base.BasePager;

/**
 * Created by sun on 2017/6/2.
 */

public class NewsPager extends BasePager {
    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initdata() {
        super.initdata();

        tv_title.setText("新闻");

        TextView textView=new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setText("新闻页面的内容");

        fl_content.addView(textView);
    }
}
