package atguigu.com.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import atguigu.com.beijingnews.Base.BasePager;
import atguigu.com.beijingnews.Base.NewsCenterMenuBasePager;
import atguigu.com.beijingnews.MainActivity;
import atguigu.com.beijingnews.NewaPagers.InteractPager;
import atguigu.com.beijingnews.NewaPagers.NewsCenterPager;
import atguigu.com.beijingnews.NewaPagers.PhotosPager;
import atguigu.com.beijingnews.NewaPagers.SubjectPager;
import atguigu.com.beijingnews.NewaPagers.VotePager;
import atguigu.com.beijingnews.domain.NewsBean;
import atguigu.com.beijingnews.fragment.LeftMenuFragment;
import atguigu.com.beijingnews.utils.ChangerUtils;
import atguigu.com.beijingnews.utils.ConstantUtils;
import okhttp3.Call;

/**
 * Created by sun on 2017/6/2.
 */

public class NewsPager extends BasePager {

    private List<NewsBean.DataBean> datas;
    private  ArrayList<NewsCenterMenuBasePager> newsPagers;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initdata() {
        super.initdata();

        tv_title.setText("新闻");
        ib_menu.setVisibility(View.VISIBLE);

        TextView textView=new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setText("新闻页面的内容");

        fl_content.addView(textView);

        String string = ChangerUtils.getString(context, ConstantUtils.NEWSCENTER_PAGER_URL);
        if(!TextUtils.isEmpty(string)){
            proressData(string);
        }


        getDataNet();
    }

    private void getDataNet() {

        String url= ConstantUtils.NEWSCENTER_PAGER_URL;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","联网失败!嘿嘿嘿!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG","联网成功!哈哈哈!");
                        ChangerUtils.putString(context,ConstantUtils.NEWSCENTER_PAGER_URL,response);
                      proressData(response);
                    }
                });
    }

    private void proressData(String json) {
        /*NewsBean newsBean = new Gson().fromJson(json, NewsBean.class);
        datas = newsBean.getData();*/
        NewsBean newsBean = new NewsBean();

        try {
            JSONObject jsonObject = new JSONObject(json);

            int retcode = jsonObject.optInt("retcode");
            newsBean.setRetcode(retcode);

            JSONArray data = jsonObject.optJSONArray("data");
            if(data!=null && data.length()>0){
                ArrayList<NewsBean.DataBean> dataBean = new ArrayList<>();
                newsBean.setData(dataBean);
                for(int i = 0; i <data.length() ; i++) {
                    JSONObject jsonObject1 = data.getJSONObject(i);

                    NewsBean.DataBean newsdata = new NewsBean.DataBean();

                    newsdata.setId(jsonObject1.optInt("id"));
                    newsdata.setTitle(jsonObject1.optString("title"));
                    newsdata.setType(jsonObject1.optInt("type"));
                    newsdata.setUrl(jsonObject1.optString("url"));

                    JSONArray children = jsonObject1.optJSONArray("children");
                    if(children!=null && children.length()>0){
                        ArrayList<NewsBean.DataBean.ChildrenBean> childrenBeen=new ArrayList<>();
                        newsdata.setChildren(childrenBeen);

                        for(int j = 0; j < children.length() ; j++) {

                            JSONObject jsonObject2 = children.getJSONObject(j);

                            NewsBean.DataBean.ChildrenBean chil = new NewsBean.DataBean.ChildrenBean();

                            chil.setId(jsonObject2.optInt("id"));
                            chil.setTitle(jsonObject2.optString("title"));
                            chil.setType(jsonObject2.optInt("type"));
                            chil.setUrl(jsonObject2.optString("url"));

                            childrenBeen.add(chil);

                        }
                    }

                    dataBean.add(newsdata);
                }

            }
            

        } catch (JSONException e) {
            e.printStackTrace();
        }

         datas = newsBean.getData();
        MainActivity main= (MainActivity) context;

        newsPagers = new ArrayList<>();
        newsPagers.add(new NewsCenterPager(context,datas.get(0).getChildren()));
        newsPagers.add(new SubjectPager(context));
        newsPagers.add(new PhotosPager(context));
        newsPagers.add(new InteractPager(context));
        newsPagers.add(new VotePager(context));

        LeftMenuFragment left= main.getLeftMenuFragment();
        left.setData(datas);





    }

    public void switchPager(int prePosition) {
        NewsCenterMenuBasePager basepagers = newsPagers.get(prePosition);
        View rootView = basepagers.rootView;

        fl_content.removeAllViews();
        fl_content.addView(rootView);
        basepagers.initData();

        tv_title.setText(datas.get(prePosition).getTitle());
    }
}
