package atguigu.com.beijingnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;

import atguigu.com.beijingnews.Base.BaseFragmnet;
import atguigu.com.beijingnews.Base.BasePager;
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

        rgMain.check(R.id.rb_home);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootView;

            basePager.initdata();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public int getCount() {
            return pagers.size();
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
