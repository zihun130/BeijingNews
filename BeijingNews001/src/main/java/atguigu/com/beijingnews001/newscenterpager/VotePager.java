package atguigu.com.beijingnews001.newscenterpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import atguigu.com.beijingnews001.base.NewsCenterBasePager;

/**
 * Created by sun on 2017/6/8.
 */

public class VotePager extends NewsCenterBasePager {
    private TextView textView;
    public VotePager(Context context) {
        super(context);
    }
    @Override
    public View initview() {
        textView=new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        return textView;
    }

    @Override
    public void initdata() {
        super.initdata();
        textView.setText("投票详情页面");
    }
}
