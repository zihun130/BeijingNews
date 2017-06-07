package atguigu.com.beijingnews.NewaPagers;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import atguigu.com.beijingnews.Base.NewsCenterMenuBasePager;
import atguigu.com.beijingnews.MainActivity;
import atguigu.com.beijingnews.R;
import atguigu.com.beijingnews.domain.NewsBean;

/**
 * Created by sun on 2017/6/3.
 */

public class SubjectPager extends NewsCenterMenuBasePager {
    private final List<NewsBean.DataBean.ChildrenBean> chilren;
    private ViewPager details_viewpager;
    private ArrayList<TabDetailPager> tabDetails;

    private TabLayout tablayout;
    private ImageButton ib_next;


    public SubjectPager(Context context, List<NewsBean.DataBean.ChildrenBean> childreanBean) {
        super(context);
        this.chilren=childreanBean;
    }

    @Override
    public View initView() {
        View view=View.inflate(context, R.layout.subject_pager_details,null);
        details_viewpager = (ViewPager) view.findViewById(R.id.details_viewpager);
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        ib_next = (ImageButton) view.findViewById(R.id.ib_next);
        //点击图片进入下一个pager
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details_viewpager.setCurrentItem(details_viewpager.getCurrentItem()+1);
            }
        });

        //屏蔽除第一个外的其他pager右滑时带出侧滑菜单
        details_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        tabDetails = new ArrayList<>();
        for(int i = 0; i <chilren.size() ; i++) {
            tabDetails.add(new TabDetailPager(context,chilren.get(i)));
        }
        //设置适配器
        details_viewpager.setAdapter(new MyDetailsPagerAdapter());
        //关联Indicator
        tablayout.setupWithViewPager(details_viewpager);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    private class MyDetailsPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return chilren.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return chilren.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetails.get(position);
            View rootView=tabDetailPager.rootView;
            container.addView(rootView);
            tabDetailPager.initData();
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

