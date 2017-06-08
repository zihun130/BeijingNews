package atguigu.com.beijingnews001.pagers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import atguigu.com.beijingnews001.MainActivity;
import atguigu.com.beijingnews001.base.BasePager;
import atguigu.com.beijingnews001.base.NewsCenterBasePager;
import atguigu.com.beijingnews001.domain.NewsPagerDataBean;
import atguigu.com.beijingnews001.fragment.LeftMenuFragment;
import atguigu.com.beijingnews001.newscenterpager.InteractPager;
import atguigu.com.beijingnews001.newscenterpager.NewsCenterPager;
import atguigu.com.beijingnews001.newscenterpager.PhotosPager;
import atguigu.com.beijingnews001.newscenterpager.SubjectPager;
import atguigu.com.beijingnews001.newscenterpager.VotePager;
import atguigu.com.beijingnews001.utils.ConstantUtils;
import okhttp3.Call;

/**
 * Created by sun on 2017/6/8.
 */

public class NewsPager extends BasePager {
    private List<NewsPagerDataBean.DataBean> datas;
    private ArrayList<NewsCenterBasePager> news;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        tv_title.setText("新闻");

        TextView textView=new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setText("新闻页面的内容");

        fl_content.addView(textView);

        getDataFromNet();
    }

    private void getDataFromNet() {
        String url= ConstantUtils.NEWSCENTER_PAGER_URL;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        processdatas(response);
                    }
                });


    }

    private void processdatas(String response) {
        NewsPagerDataBean bean = new Gson().fromJson(response, NewsPagerDataBean.class);
        datas = bean.getData();

        news=new ArrayList<>();
        news.add(new NewsCenterPager(context));
        news.add(new SubjectPager(context));
        news.add(new PhotosPager(context));
        news.add(new InteractPager(context));
        news.add(new VotePager(context));

        MainActivity main= (MainActivity) context;

        LeftMenuFragment left = main.getLeftMenuFragment();

        left.setData(datas);

    }

    public void switchpager(int preposition) {
        NewsCenterBasePager newsCenterBasePager = news.get(preposition);
        View rootview = newsCenterBasePager.rootview;

        fl_content.removeAllViews();
        fl_content.addView(rootview);
        newsCenterBasePager.initdata();

        tv_title.setText(datas.get(preposition).getTitle());


    }
}
