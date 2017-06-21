package ru.luna_koly.jetbrainsproject;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import ru.luna_koly.jetbrainsproject.activityTemplates.NoTitleBarActivity;
import ru.luna_koly.jetbrainsproject.fragments.SettingsFragment;
import ru.luna_koly.jetbrainsproject.util.LoadingInitializer;

public class MainActivity extends NoTitleBarActivity {
    private static final String TAG = "main_activity";
    private static final long MOVE_ANIM_TIME = 500;

    private LinearLayout fragmentHolder;
    private boolean isFragmentHolderOpen = false;

    private Animation leftIn, leftOut;

    private ImageButton settings;
    private SettingsFragment settingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFragmentHolder();
        setupButtonListeners();

        Log.d(TAG, "Created");
    }

    private void setupFragmentHolder() {
        fragmentHolder = (LinearLayout) findViewById(R.id.fragment_holder);

        leftIn = AnimationUtils.loadAnimation(this, R.anim.move_left_in);
        leftOut = AnimationUtils.loadAnimation(this, R.anim.move_left_out);

        FragmentManager fm = getFragmentManager();
        settingsFragment = (SettingsFragment) fm.findFragmentById(R.id.settings_fragment);

        Log.d(TAG, "Fragment : OK");
    }

    private void setupButtonListeners() {
        ImageButton play = (ImageButton) findViewById(R.id.bt_play);
        ImageButton inventory = (ImageButton) findViewById(R.id.bt_inventory);
        ImageButton diary = (ImageButton) findViewById(R.id.bt_diary);
        settings = (ImageButton) findViewById(R.id.bt_settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerFragmentHolder();
            }
        });

        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingInitializer.loadData();
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        @SuppressWarnings("ConstantConditions")
        ImageButton delete = (ImageButton) settingsFragment.getView().findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingInitializer.clearData();
            }
        });

        Log.d(TAG, "Buttons : OK");
    }

    private void disableImageButtonFor(final ImageButton button, long delay) {
        button.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(true);
            }
        }, delay);
    }

    private void triggerFragmentHolder() {
        disableImageButtonFor(settings, MOVE_ANIM_TIME);

        if (isFragmentHolderOpen) {
            isFragmentHolderOpen = false;
            closeFragmentHolder();
            return;
        }


        isFragmentHolderOpen = true;
        openFragmentHolder();
    }

    private void openFragmentHolder() {
        fragmentHolder.startAnimation(leftIn);
    }

    private void closeFragmentHolder() {
        fragmentHolder.startAnimation(leftOut);
    }

}
