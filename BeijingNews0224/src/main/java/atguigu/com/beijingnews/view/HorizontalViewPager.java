package atguigu.com.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sun on 2017/6/5.
 */

public class HorizontalViewPager extends ViewPager {
    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

   private float startX;

   private float startY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                startX=ev.getX();
                startY=ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE :
                float endX=ev.getX();
                float endY=ev.getY();
                float distanceX=Math.abs(endX-startX);
                float distanceY=Math.abs(endY-startY);

                if(distanceX > distanceY && distanceX>8){
                    if((endX-startX > 0)&& getCurrentItem()==0){
                        //从左到右,0位置
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if((endX-startX<0)&& getCurrentItem()==getAdapter().getCount()-1){
                        //从右到左,最后位置
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else {
                        //中间部分
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                }else {
                    //竖直方向
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP :
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
