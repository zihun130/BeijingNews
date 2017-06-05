package atguigu.com.beijingnews.NewaPagers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import atguigu.com.beijingnews.Base.NewsCenterMenuBasePager;
import atguigu.com.beijingnews.MainActivity;
import atguigu.com.beijingnews.R;
import atguigu.com.beijingnews.domain.NewsBean;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sun on 2017/6/5.
 */

public class NewsCenterPager002 extends NewsCenterMenuBasePager {

    private final List<NewsBean.DataBean.ChildrenBean> child;
    @InjectView(R.id.indicator)
    TabPageIndicator indicator;
    @InjectView(R.id.ib_next)
    ImageButton ibNext;
    @InjectView(R.id.details_viewpager)
    ViewPager detailsViewpager;


    private ArrayList<TabDetailPager002> tab;
    private MyDetailsAdapter adapter;

    public NewsCenterPager002(Context context, List<NewsBean.DataBean.ChildrenBean> childreanBean) {
        super(context);
        this.child = childreanBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.news_center_pager_details, null);
        ButterKnife.inject(context,view);

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsViewpager.setCurrentItem(detailsViewpager.getCurrentItem()+1);
            }
        });


        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               if(position==0){
                   MainActivity mainActivity= (MainActivity) context;
                   mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
               }else {
                   MainActivity mainActivity= (MainActivity) context;
                   mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
               }
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

        tab = new ArrayList<>();
        for(int i = 0; i < child.size(); i++) {

            tab.add(new TabDetailPager002(context,child.get(i)));
        }

        adapter=new MyDetailsAdapter();
        detailsViewpager.setAdapter(adapter);
        //关联
        indicator.setViewPager(detailsViewpager);
    }

    class MyDetailsAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return child.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager002 tabDetailPager002 = tab.get(position);
            View rootView = tabDetailPager002.rootView;
            container.addView(rootView);
            return rootView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
