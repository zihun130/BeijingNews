package atguigu.com.beijingnews001.fragment;


import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import atguigu.com.beijingnews001.MainActivity;
import atguigu.com.beijingnews001.R;
import atguigu.com.beijingnews001.base.BaseFragment;
import atguigu.com.beijingnews001.domain.NewsPagerDataBean;
import atguigu.com.beijingnews001.pagers.NewsPager;

/**
 * Created by sun on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragment {
    private ListView listview;
    private List<NewsPagerDataBean.DataBean> datas;
    private MyLeftMenuAdapter adapter;
    private int preposition=0;

    @Override
    public View initview() {
        listview=new ListView(context);
        listview.setPadding(0,40,0,0);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    preposition=position;
                    adapter.notifyDataSetChanged();

                MainActivity main= (MainActivity) context;
                main.getSlidingMenu().toggle();

                ContentFragment content = main.getContentFragment();
                NewsPager newsPager = content.getNewsPager();

                newsPager.switchpager(preposition);

            }
        });
        return listview;
    }

    @Override
    public void initdata() {
        super.initdata();
    }

    public void setData(List<NewsPagerDataBean.DataBean> datas) {
        this.datas=datas;

        adapter=new MyLeftMenuAdapter();
        listview.setAdapter(adapter);

        MainActivity mainActivity= (MainActivity) context;

        ContentFragment content = mainActivity.getContentFragment();
        NewsPager newsPager = content.getNewsPager();

        newsPager.switchpager(preposition);

    }

    private class MyLeftMenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas==null ? 0 : datas.size();
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
            TextView textView= (TextView) View.inflate(context, R.layout.item_left_menu,null);

            if(preposition==position){
                textView.setEnabled(true);
            }else {
                textView.setEnabled(false);
            }


            NewsPagerDataBean.DataBean dataBean = datas.get(position);
            textView.setText(dataBean.getTitle());
            return textView;
        }
    }
}
