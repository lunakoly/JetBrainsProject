package ru.luna_koly.jetbrainsproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.Random;

import ru.luna_koly.jetbrainsproject.activityTemplates.NoTitleBarActivity;
import ru.luna_koly.jetbrainsproject.fragments.PlayFragment;
import ru.luna_koly.jetbrainsproject.fragments.SettingsFragment;

public class MainActivity extends NoTitleBarActivity {
    private static final String TAG = "main_activity";
    private static final long WALLPAPER_TIME = 10000;
    private static final long MOVE_ANIM_TIME = 500;

    private LinearLayout fragmentHolder;
    private boolean isFragmentHolderOpen = false;
    private FragmentHolderState currentFragmentHolderState = FragmentHolderState.SETTINGS;

    private Animation leftIn, leftOut;

    private ImageButton play, inventory, diary, settings;
    private FragmentManager fm;
    private SettingsFragment settingsFragment;
    private PlayFragment playFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadingInitializer loadManager = new LoadingInitializer(this);

        setupFragmentHolder();
        setupButtonListeners();
        setupBGSwitcher();

        Log.d(TAG, "Created");
    }

    private void setupFragmentHolder() {
        fragmentHolder = (LinearLayout) findViewById(R.id.fragment_holder);

        leftIn = AnimationUtils.loadAnimation(this, R.anim.move_left_in);
        leftOut = AnimationUtils.loadAnimation(this, R.anim.move_left_out);

        fm = getFragmentManager();
        settingsFragment = (SettingsFragment) fm.findFragmentById(R.id.settings_fragment);
        playFragment = (PlayFragment) fm.findFragmentById(R.id.play_fragment);

        showHideFragments(settingsFragment, playFragment);
        Log.d(TAG, "Fragment : OK");
    }

    private void setupBGSwitcher() {
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
                R.drawable.small_basic_wallpaper_3,
                R.drawable.small_wallpaper_1
        };

        // run bg
        final Handler handler = new Handler();
        final Random random = new Random();
        handler.postDelayed(new Runnable() {
            public void run() {
                int randomNum = random.nextInt(backgrounds.length);
                bg.setImageResource(backgrounds[randomNum]);
                handler.postDelayed(this, WALLPAPER_TIME);
            }
        }, WALLPAPER_TIME);

        Log.d(TAG, "BGSwitcher : OK");
    }

    private void setupButtonListeners() {
        play = (ImageButton) findViewById(R.id.bt_play);
        inventory = (ImageButton) findViewById(R.id.bt_inventory);
        diary = (ImageButton) findViewById(R.id.bt_diary);
        settings = (ImageButton) findViewById(R.id.bt_settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFragmentHolderOpen || currentFragmentHolderState == FragmentHolderState.SETTINGS) {
                    triggerFragmentHolder();
                }

                setFragmentHolderState(FragmentHolderState.SETTINGS);
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
                if (!isFragmentHolderOpen || currentFragmentHolderState == FragmentHolderState.PLAY) {
                    triggerFragmentHolder();
                }

                setFragmentHolderState(FragmentHolderState.PLAY);
            }
        });

        setPlayButtons();

        Log.d(TAG, "Buttons : OK");
    }

    private void setPlayButtons() {
        View save1 = playFragment.getView().findViewById(R.id.save1);
        View save2 = playFragment.getView().findViewById(R.id.save2);

        ImageButton play1 = (ImageButton) save1.findViewById(R.id.play_button);
        ImageButton play2 = (ImageButton) save2.findViewById(R.id.play_button);

        play1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingInitializer.playProfile(MainActivity.this, 0);
                closeFragmentHolder();
            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingInitializer.playProfile(MainActivity.this, 1);
                closeFragmentHolder();
            }
        });

        ImageButton delete1 = (ImageButton) save1.findViewById(R.id.delete_button);
        ImageButton delete2 = (ImageButton) save2.findViewById(R.id.delete_button);

        delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingInitializer.deleteProfile(MainActivity.this, 0);
            }
        });

        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingInitializer.deleteProfile(MainActivity.this, 1);
            }
        });

        LoadingInitializer.update(save1);
        LoadingInitializer.update(save2);
    }

    private void disableButtonsFor(long delay) {
        disableImageButtonFor(inventory, delay);
        disableImageButtonFor(play, delay);
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
        disableButtonsFor(MOVE_ANIM_TIME);

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

    private void showHideFragments(Fragment toShow, Fragment... toHide) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(toShow)
                .commit();

        for (Fragment f : toHide)
            fm.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .hide(f)
                    .commit();
    }

    public void setFragmentHolderState(FragmentHolderState fragmentHolderState) {
        this.currentFragmentHolderState = fragmentHolderState;

        Log.d("holder", "" + fragmentHolderState);

        switch (fragmentHolderState) {
            case SETTINGS:
                showHideFragments(settingsFragment, playFragment);
                break;
            case PLAY:
                showHideFragments(playFragment, settingsFragment);
                break;
        }
    }


    private enum FragmentHolderState {
        SETTINGS, PLAY
    }

}
