package atguigu.com.beijingnews.NewaPagers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import atguigu.com.beijingnews.Base.NewsCenterMenuBasePager;
import atguigu.com.beijingnews.R;
import atguigu.com.beijingnews.domain.DetailsPagerBean;
import atguigu.com.beijingnews.domain.NewsBean;
import atguigu.com.beijingnews.utils.ConstantUtils;
import atguigu.com.beijingnews.utils.DensityUtil;
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
    private final NewsBean.DataBean.ChildrenBean childrenBean;

    private ViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    @InjectView(R.id.lv)
    ListView lv;

    private int prePosition = 0;
    private List<DetailsPagerBean.DataBean.NewsBean> news;


    private String url;
    private List<DetailsPagerBean.DataBean.TopnewsBean> topmews;

    public TabDetailPager(Context context, NewsBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);

        View viewTop = View.inflate(context, R.layout.listview_topview_item, null);
        viewpager = (ViewPager) viewTop.findViewById(R.id.viewpager);
        tv_title = (TextView) viewTop.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) viewTop.findViewById(R.id.ll_point_group);

        lv.addHeaderView(viewTop);
        //ViewPager页面变换监听
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置标题
                String title = topmews.get(position).getTitle();
                tv_title.setText(title);
                //设置红点的选项
                ll_point_group.getChildAt(prePosition).setEnabled(false);
                ll_point_group.getChildAt(position).setEnabled(true);
                prePosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据
        url = ConstantUtils.BASE_URL + childrenBean.getUrl();

        getDatasFromNet();
    }

    private void getDatasFromNet() {
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
                    }


                });
    }

    private void processData(String response) {
        DetailsPagerBean bean = new Gson().fromJson(response, DetailsPagerBean.class);

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
        lv.setAdapter(new MyListViewAdapter());
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
