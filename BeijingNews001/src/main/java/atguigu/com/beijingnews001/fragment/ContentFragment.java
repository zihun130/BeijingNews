package atguigu.com.beijingnews001.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import atguigu.com.beijingnews001.MainActivity;
import atguigu.com.beijingnews001.R;
import atguigu.com.beijingnews001.base.BaseFragment;
import atguigu.com.beijingnews001.base.BasePager;
import atguigu.com.beijingnews001.pagers.HomePager;
import atguigu.com.beijingnews001.pagers.NewsPager;
import atguigu.com.beijingnews001.pagers.SettingPager;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sun on 2017/6/2.
 */

public class ContentFragment extends BaseFragment {
    @InjectView(R.id.vpager)
    ViewPager vpager;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;
    private ArrayList<BasePager> pagers;

    @Override
    public View initview() {
        View view = View.inflate(context, R.layout.fragment_content, null);
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

        vpager.setAdapter(new MyBasePagerAdapter());

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
                pagers.get(position).initData();
                if(position==1){
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

        MainActivity mainActivity= (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        pagers.get(0).initData();
        rgMain.check(R.id.rb_home);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public NewsPager getNewsPager() {
        return (NewsPager) pagers.get(1);
    }


    private class MyBasePagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootview;
            container.addView(rootView);
            return rootView;
        }

        @Override
        public int getCount() {
            return pagers==null ? 0 : pagers.size();
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
