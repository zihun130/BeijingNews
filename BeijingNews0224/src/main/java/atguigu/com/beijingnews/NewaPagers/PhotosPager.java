package atguigu.com.beijingnews.NewaPagers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import atguigu.com.beijingnews.Base.NewsCenterMenuBasePager;

/**
 * Created by sun on 2017/6/3.
 */

public class PhotosPager extends NewsCenterMenuBasePager {
    private TextView textView;
    public PhotosPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView=new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("图组详情页面的内容");
    }
}
