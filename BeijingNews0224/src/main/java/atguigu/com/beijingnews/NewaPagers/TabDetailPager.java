package atguigu.com.beijingnews.NewaPagers;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import atguigu.com.beijingnews.Base.NewsCenterMenuBasePager;
import atguigu.com.beijingnews.R;
import atguigu.com.beijingnews.domain.DetailsPagerBean;
import atguigu.com.beijingnews.domain.NewsBean;
import atguigu.com.beijingnews.utils.ChangerUtils;
import atguigu.com.beijingnews.utils.ConstantUtils;
import atguigu.com.beijingnews.utils.DensityUtil;
import atguigu.com.beijingnews.view.HorizontalViewPager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 作者：杨光福 on 2017/6/5 09:24
 * QQ：541433511
 * 微信：yangguangfu520
 * 作用：
 */

public class TabDetailPager extends NewsCenterMenuBasePager {
    public static final String READ_NEWS_ID_ARRAY_KEY = "read_news_id_array_key";
    private final NewsBean.DataBean.ChildrenBean childrenBean;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView pullRefreshList;

    private HorizontalViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private ListView lv;

    private int prePosition = 0;
    private List<DetailsPagerBean.DataBean.NewsBean> news;


    private String url;
    private List<DetailsPagerBean.DataBean.TopnewsBean> topmews;
    private String moreUrl;
    private boolean isLoadingMore=false;
    private MyListViewAdapter adapter;

    public TabDetailPager(Context context, NewsBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);
        //从下拉刷新中获取ListView
        lv=pullRefreshList.getRefreshableView();


        /**
         * Add Sound Event Listener
         */
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(context);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pullRefreshList.setOnPullEventListener(soundListener);
        //顶部视图
        View viewTop = View.inflate(context, R.layout.listview_topview_item, null);
        viewpager = (HorizontalViewPager) viewTop.findViewById(R.id.viewpager);
        tv_title = (TextView) viewTop.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) viewTop.findViewById(R.id.ll_point_group);

        lv.addHeaderView(viewTop);
        //ListView每一条的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int realPosition=position - 2;
                DetailsPagerBean.DataBean.NewsBean newsBean = news.get(realPosition);

                String newsarray = ChangerUtils.getString(context,READ_NEWS_ID_ARRAY_KEY);
                //判断存储的集合中是否包含新取到的ID
                if(!newsarray.contains(newsBean.getId()+"")){
                    //如果没有,获取新的ID
                    String currValue=newsarray+newsBean.getId()+",";
                    //把新的ID存到集合中
                    ChangerUtils.putString(context,READ_NEWS_ID_ARRAY_KEY,currValue);
                    adapter.notifyDataSetChanged();
                }

            }
        });


        //ViewPager页面变换监听
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //设置红点的选项,添加在这个位置是解决0位置指示点一直存在的问题
                ll_point_group.getChildAt(prePosition).setEnabled(false);
                ll_point_group.getChildAt(position).setEnabled(true);
                prePosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                //设置标题
                String title = topmews.get(position).getTitle();
                tv_title.setText(title);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //下拉刷新与上拉加载
        pullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadingMore=false;
                getDatasFromNet(url);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                 if(!TextUtils.isEmpty(moreUrl)){
                     isLoadingMore=true;
                     getDatasFromNet(moreUrl);

                 }else{
                     Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
                 }
            }
        });





        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据
        url = ConstantUtils.BASE_URL + childrenBean.getUrl();

        getDatasFromNet(url);
    }

    private void getDatasFromNet(String url) {
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
           topmews = bean.getData().getTopnews();

           viewpager.setAdapter(new MyTopNewsAdapter());
           //设置默认首页
           tv_title.setText(topmews.get(prePosition).getTitle());
           //只有先移除视图,在加载,才能不重复
           ll_point_group.removeAllViews();
           //设置线性横向布局的点
           for (int i = 0; i < topmews.size(); i++) {
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

           //设置ListView适配器
           news = bean.getData().getNews();
           adapter=new MyListViewAdapter();
           lv.setAdapter(adapter);

       }else {
           isLoadingMore=false;
           //把新数据放入集合
           news.addAll(bean.getData().getNews());
           adapter.notifyDataSetChanged();
       }

    }

    private class MyTopNewsAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topmews == null ? 0 : topmews.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.pic_item_list_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            String imageurl = ConstantUtils.BASE_URL + topmews.get(position).getTopimage();
            Glide.with(context)
                    .load(imageurl)
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

    //ListView适配器
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
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
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

            String newsarray = ChangerUtils.getString(context,READ_NEWS_ID_ARRAY_KEY);
            if(newsarray.contains(newsBean.getId()+"")){
                viewHolder.tvDesc.setTextColor(Color.GRAY);
            }else {
                viewHolder.tvDesc.setTextColor(Color.BLACK);
            }


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
