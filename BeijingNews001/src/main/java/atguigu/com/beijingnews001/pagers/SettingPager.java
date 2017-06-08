package atguigu.com.beijingnews001.pagers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import atguigu.com.beijingnews001.base.BasePager;

/**
 * Created by sun on 2017/6/8.
 */

public class SettingPager extends BasePager {
    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("设置");

        TextView textView=new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setText("设置页面的内容");

        fl_content.addView(textView);
    }
}
