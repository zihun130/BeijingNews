package atguigu.com.beijingnews001;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import atguigu.com.beijingnews001.utils.ChangerUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.btn_start_main)
    Button btnStartMain;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.iv_red_point)
    ImageView ivRedPoint;
    @InjectView(R.id.activity_guide)
    RelativeLayout activityGuide;

    private ArrayList<ImageView> imageViews;
    private int[] ids = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private MyViewPagerAdapter adapter;
    private int leftMargin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        initdata();
        adapter=new MyViewPagerAdapter();
        viewpager.setAdapter(adapter);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float left=leftMargin*(position+positionOffset);
                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                params.leftMargin= (int) left;
                ivRedPoint.setLayoutParams(params);

            }

            @Override
            public void onPageSelected(int position) {
                if(position==imageViews.size()-1){
                    btnStartMain.setVisibility(View.VISIBLE);
                }else {
                    btnStartMain.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                leftMargin=llPointGroup.getChildAt(1).getLeft()-llPointGroup.getChildAt(0).getLeft();
            }
        });
    }

    private void initdata() {
       imageViews= new ArrayList<>();
        for(int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);
            imageViews.add(imageView);

            ImageView point=new ImageView(this);
            point.setImageResource(R.drawable.guide_point_normal);

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(10,10);
            point.setLayoutParams(params);
            if(i!=0){
                params.leftMargin=10;
            }
            llPointGroup.addView(point);
        }


    }

    @OnClick(R.id.btn_start_main)
    public void onViewClicked() {
        ChangerUtils.putBoolean(this,"start_main",true);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private class MyViewPagerAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return imageViews.size();
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
