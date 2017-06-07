package atguigu.com.beijingnews.NewaPagers;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import atguigu.com.beijingnews.Base.NewsCenterMenuBasePager;
import atguigu.com.beijingnews.R;
import atguigu.com.beijingnews.adapter.PhotosRecyclerViewAdapter;
import atguigu.com.beijingnews.domain.NewsBean;
import atguigu.com.beijingnews.domain.PhotosBean;
import atguigu.com.newsbeijing_library.utils.ChangerUtils;
import atguigu.com.newsbeijing_library.utils.ConstantUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by sun on 2017/6/3.
 */

public class PhotosPager extends NewsCenterMenuBasePager {
    private final NewsBean.DataBean databean;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private String url;
    private List<PhotosBean.DataBean.NewsBean> news;
    private PhotosRecyclerViewAdapter adapter;
    private boolean isShowList = true;

    public PhotosPager(Context context, NewsBean.DataBean dataBean) {
        super(context);
        this.databean = dataBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photo_list_glid, null);
        ButterKnife.inject(this, view);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPhotosFromNet(url);
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        url = ConstantUtils.BASE_URL + databean.getUrl();

        String save = ChangerUtils.getString(context, url);
        if(!TextUtils.isEmpty(save)){
            processData(save);
        }
        getPhotosFromNet(url);
    }

    private void getPhotosFromNet(final String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "图组请求失败==" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "图组请求成功==");

                        ChangerUtils.putString(context,url,response);
                        processData(response);

                    }


                });


    }
    //解析数据
    private void processData(String response) {
        PhotosBean photosBean = new Gson().fromJson(response, PhotosBean.class);

        news = photosBean.getData().getNews();
        if (news != null && news.size() > 0) {
            progressbar.setVisibility(View.GONE);

            adapter = new PhotosRecyclerViewAdapter(context, news,recyclerview);
            recyclerview.setAdapter(adapter);


            //布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        } else {
            progressbar.setVisibility(View.VISIBLE);
        }

        //结束刷新
        refreshLayout.setRefreshing(false);
    }

    public void SwitchListOrGrid(ImageButton imager) {
        if (isShowList) {
            isShowList = false;
            recyclerview.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
            imager.setImageResource(R.drawable.icon_pic_list_type);
        } else {
            isShowList = true;
            recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            imager.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }
}
