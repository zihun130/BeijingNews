package atguigu.com.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import atguigu.com.beijingnews.Base.BasePager;

/**
 * Created by sun on 2017/6/2.
 */

public class SettingPager extends BasePager {
    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initdata() {
        super.initdata();

        tv_title.setText("设置");

        TextView textView=new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setText("设置页面的内容");

        fl_content.addView(textView);
    }
}
