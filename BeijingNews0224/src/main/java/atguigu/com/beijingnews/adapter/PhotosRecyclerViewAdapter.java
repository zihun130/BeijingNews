package atguigu.com.beijingnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import atguigu.com.beijingnews.PicassoSampleActivity;
import atguigu.com.beijingnews.R;
import atguigu.com.beijingnews.domain.PhotosBean;
import atguigu.com.newsbeijing_library.utils.BitmapCacheUtils;
import atguigu.com.newsbeijing_library.utils.ConstantUtils;
import atguigu.com.newsbeijing_library.utils.NetCachUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sun on 2017/6/6.
 */

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private final List<PhotosBean.DataBean.NewsBean> news;
    private final RecyclerView recyclerview;

    private BitmapCacheUtils bitmapCacheUtils;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCachUtils.SUCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;

                    ImageView viewWithTag = (ImageView) recyclerview.findViewWithTag(position);
                    if(viewWithTag!=null && bitmap!=null){
                        viewWithTag.setImageBitmap(bitmap);
                    }
                    break;
                case NetCachUtils.FAIL:
                    position=msg.arg1;
                    break;
            }
        }
    };

    public PhotosRecyclerViewAdapter(Context context, List<PhotosBean.DataBean.NewsBean> news, RecyclerView recyclerview) {
        this.context = context;
        this.news = news;
        bitmapCacheUtils=new BitmapCacheUtils(handler);
        this.recyclerview=recyclerview;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.photos_recyclerview_item, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PhotosBean.DataBean.NewsBean newsBean = news.get(position);
        holder.tvTitle.setText(newsBean.getTitle());
        String imageUrl = ConstantUtils.BASE_URL+newsBean.getListimage();
        //Glid,Picasso等会自动进行三级缓存
       /* Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.pic_item_list_default)
                .error(R.drawable.pic_item_list_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);*/
       //自定义设置三级缓存
        Bitmap bitmap= bitmapCacheUtils.getBitMap(imageUrl,position);
        //图片对应的Tag就是位置
        holder.ivIcon.setTag(position);
        if(bitmap!=null){//来自内存或本地,不会来自网络
            holder.ivIcon.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return news == null ? 0 : news.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PicassoSampleActivity.class);
                    String url=ConstantUtils.BASE_URL+news.get(getLayoutPosition()).getListimage();
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
        }
    }

}
