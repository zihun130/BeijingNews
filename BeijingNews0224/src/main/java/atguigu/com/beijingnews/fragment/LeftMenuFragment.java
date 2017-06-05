package atguigu.com.beijingnews.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import atguigu.com.beijingnews.Base.BaseFragmnet;
import atguigu.com.beijingnews.MainActivity;
import atguigu.com.beijingnews.R;
import atguigu.com.beijingnews.domain.NewsBean;
import atguigu.com.beijingnews.pager.NewsPager;

/**
 * Created by sun on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragmnet {

    private List<NewsBean.DataBean> datas;
    private ListView listView;
    private NewsCenterAdapter adapter;
    private int prePosition=0;

    @Override
    public View initView() {
        listView = new ListView(context);
        listView.setPadding(0,40,0,0);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prePosition=position;
                adapter.notifyDataSetChanged();

                MainActivity mainActivity= (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();

               ContentFragment contentFragment= mainActivity.getContentFragment();

               NewsPager newsPager= contentFragment.getNewsPager();
                newsPager.switchPager(prePosition);


            }
        });

        return listView;
    }

    @Override
    public void initdata() {
        super.initdata();
    }


    public void setData(List<NewsBean.DataBean> datas) {
        this.datas=datas;

        adapter=new NewsCenterAdapter();
        listView.setAdapter(adapter);

        MainActivity mainActivity= (MainActivity) context;
        ContentFragment contentFragment= mainActivity.getContentFragment();

        NewsPager newsPager= contentFragment.getNewsPager();
        newsPager.switchPager(prePosition);
    }

    private class NewsCenterAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas.size();
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
            TextView textView= (TextView) View.inflate(context, R.layout.item_leftmenu,null);

            if(prePosition==position){
                textView.setEnabled(true);
            }else {
                textView.setEnabled(false);
            }

            NewsBean.DataBean dataBean = datas.get(position);
            textView.setText(dataBean.getTitle());
            return textView;
        }
    }
}
