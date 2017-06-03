package atguigu.com.beijingnews.fragment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import atguigu.com.beijingnews.Base.BaseFragmnet;
import atguigu.com.beijingnews.Base.BasePager;
import atguigu.com.beijingnews.MainActivity;
import atguigu.com.beijingnews.R;
import atguigu.com.beijingnews.pager.HomePager;
import atguigu.com.beijingnews.pager.NewsPager;
import atguigu.com.beijingnews.pager.SettingPager;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sun on 2017/6/2.
 */

public class ContentFragment extends BaseFragmnet {
    @InjectView(R.id.vpager)
    ViewPager vpager;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    private ArrayList<BasePager> pagers;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragmnet_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initdata() {
        super.initdata();
        pagers=new ArrayList<>();
        pagers.add(new HomePager(context));
        pagers.add(new NewsPager(context));
        pagers.add(new SettingPager(context));

        vpager.setAdapter(new MyPagerAdapter());

        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 switch (checkedId) {
                     case R.id.rb_home :
                         vpager.setCurrentItem(0,false);
                         break;
                     case R.id.rb_news :
                         vpager.setCurrentItem(1,false);
                         break;
                     case R.id.rb_setting :
                         vpager.setCurrentItem(2,false);

                         break;
                 }
            }
        });


        vpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               pagers.get(position).initdata();
                if(position==1){
                    isEnableSlidingMenu(context, SlidingMenu.TOUCHMODE_FULLSCREEN);
                }else {
                    isEnableSlidingMenu((MainActivity) context, SlidingMenu.TOUCHMODE_NONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        isEnableSlidingMenu((MainActivity) context, SlidingMenu.TOUCHMODE_NONE);
        pagers.get(0).initdata();
        rgMain.check(R.id.rb_home);
    }

    private static void isEnableSlidingMenu(Context context, int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private class MyPagerAdapter extends PagerAdapter {
        //创建Item布局,把当前视图添加进容器
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootView;

            container.addView(rootView);
            return rootView;
        }
        //得到总数
        @Override
        public int getCount() {
            return pagers.size();
        }
        //判断当前view与instantiateItem返回的是否是同一view
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        //摧毁视图
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
