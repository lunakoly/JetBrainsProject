package ru.luna_koly.jetbrainsproject;

import android.app.ActionBar.LayoutParams;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import ru.luna_koly.jetbrainsproject.activityTemplates.NoTitleBarActivity;

public class MainActivity extends NoTitleBarActivity {
    final private long wallpaperTime = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init bg
        final ImageSwitcher bg = (ImageSwitcher) findViewById(R.id.bg);
        // set fade animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        bg.setInAnimation(fadeIn);
        bg.setOutAnimation(fadeOut);

        bg.setFactory(new ViewSwitcher.ViewFactory() {
                public View makeView() {
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    return imageView;
                }
            }
        );

        // start bg animations
        final int[] backgrounds = {
                R.drawable.small_basic_wallpaper_1,
                R.drawable.small_basic_wallpaper_2,
                R.drawable.small_basic_wallpaper_3
        };

        final Handler handler = new Handler();
        final Random random = new Random();
        handler.postDelayed(new Runnable() {
            public void run() {
                int randomNum = random.nextInt(backgrounds.length);
                bg.setImageResource(backgrounds[randomNum]);
                handler.postDelayed(this, wallpaperTime);
            }
        }, wallpaperTime);

    }

}
