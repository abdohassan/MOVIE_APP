package com.example.engab.movie_app.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import static uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper.wrap;
import com.example.engab.movie_app.R;
import com.example.engab.movie_app.fragment.DetailFragment;
import com.example.engab.movie_app.fragment.MoviesFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import com.example.engab.movie_app.R;
public class MainActivity extends AppCompatActivity {

    public static RelativeLayout cornStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Calligraphy lib.
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_main);

        // Initialize UIL.
        ImageLoaderConfiguration mConfiguration =
                new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(mConfiguration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarLayout bar = (AppBarLayout) findViewById(R.id.appBar);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainRel);
        bar.measure(WRAP_CONTENT, WRAP_CONTENT);
        int num = bar.getMeasuredHeight();
        layout.setPadding(0, num, 0, 0);
        cornStamp = (RelativeLayout) findViewById(R.id.cornStamp);

    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(wrap(newBase));
    }
}

