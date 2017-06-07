package atguigu.com.beijingnews;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;


public class PicassoSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        final PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
        Uri data = getIntent().getData();

        Picasso.with(this)
                .load(data)
                .into(photoView);
    }
}
