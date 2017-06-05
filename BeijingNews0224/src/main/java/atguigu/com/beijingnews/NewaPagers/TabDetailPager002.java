package atguigu.com.beijingnews.NewaPagers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import atguigu.com.beijingnews.Base.NewsCenterMenuBasePager;
import atguigu.com.beijingnews.R;
import atguigu.com.beijingnews.domain.DetailsPagerBean;
import atguigu.com.beijingnews.domain.NewsBean;
import atguigu.com.beijingnews.utils.ConstantUtils;
import atguigu.com.beijingnews.utils.DensityUtil;
import atguigu.com.beijingnews.view.HorizontalViewPager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by sun on 2017/6/5.
 */

public class TabDetailPager002 extends NewsCenterMenuBasePager {
    private final NewsBean.DataBean.ChildrenBean childrenBean;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView pullRefreshList;

    private ListView lv;
    private String url;
    private List<DetailsPagerBean.DataBean.TopnewsBean> topnews;
    private HorizontalViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private MyTopViewpagerAdapter adapter;
    private int prePosition = 0;
    private List<DetailsPagerBean.DataBean.NewsBean> news;
    private MyListViewAdapter newsadapter;
    private boolean isLoadingMore;
    private String moreUrl;

    public TabDetailPager002(Context context, NewsBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(context, view);

        lv = pullRefreshList.getRefreshableView();

        View viewTop = View.inflate(context, R.layout.listview_topview_item, null);
        viewpager = (HorizontalViewPager) viewTop.findViewById(R.id.viewpager);
        tv_title = (TextView) viewTop.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) viewTop.findViewById(R.id.ll_point_group);
        lv.addHeaderView(viewTop);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = topnews.get(position).getTitle();
                tv_title.setText(title);

                ll_point_group.getChildAt(prePosition).setEnabled(false);
                ll_point_group.getChildAt(position).setEnabled(true);
                prePosition=position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        pullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadingMore=false;
                getDataFromNet(url);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!TextUtils.isEmpty(moreUrl)){
                    isLoadingMore=true;
                    getDataFromNet(moreUrl);
                }else {
                    Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        url = ConstantUtils.BASE_URL + childrenBean.getUrl();

        getDataFromNet(url);
    }

    private void getDataFromNet(String url) {
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
                        processData(response);
                        //结束上拉加载和下拉刷新
                        pullRefreshList.onRefreshComplete();
                    }


                });
    }

    private void processData(String response) {
        DetailsPagerBean bean = new Gson().fromJson(response, DetailsPagerBean.class);

        String more = bean.getData().getMore();
        if(!TextUtils.isEmpty(more)){
            moreUrl=ConstantUtils.BASE_URL+more;
        }

        if(!isLoadingMore){
            topnews = bean.getData().getTopnews();

            adapter = new MyTopViewpagerAdapter();
            viewpager.setAdapter(adapter);

            tv_title.setText(topnews.get(prePosition).getTitle());

            for (int i = 0; i < topnews.size(); i++) {
                ImageView point = new ImageView(context);
                point.setBackgroundResource(R.drawable.point_selector);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 8), DensityUtil.dip2px(context, 8));
                point.setLayoutParams(params);

                if (i == 0) {
                    point.setEnabled(true);
                } else {
                    point.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(context, 8);
                }
                ll_point_group.addView(point);

            }

            news = bean.getData().getNews();
            newsadapter = new MyListViewAdapter();
            lv.setAdapter(newsadapter);
        }else {
            isLoadingMore=false;
            news.addAll(bean.getData().getNews());
            newsadapter.notifyDataSetChanged();
        }

    }

    private class MyTopViewpagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.pic_item_list_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            String imagerurl = ConstantUtils.BASE_URL + topnews.get(position).getTopimage();
            Glide.with(context)
                    .load(imagerurl)
                    .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //ListView的适配器
    private class MyListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.details_listview_item, null);
                viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            DetailsPagerBean.DataBean.NewsBean newsBean = news.get(position);
            viewHolder.tvDesc.setText(newsBean.getTitle());
            viewHolder.tvTime.setText(newsBean.getPubdate());

            String imageurl = ConstantUtils.BASE_URL + newsBean.getListimage();
            Glide.with(context)
                    .load(imageurl)
                    .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivIcon);

            return convertView;
        }

    }

    static class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_desc)
        TextView tvDesc;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
