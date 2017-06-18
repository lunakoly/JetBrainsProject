package ru.luna_koly.jetbrainsproject;

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
import android.widget.ViewSwitcher;

import java.util.Random;

import ru.luna_koly.jetbrainsproject.activityTemplates.NoTitleBarActivity;

public class MainActivity extends NoTitleBarActivity {
    final private long wallpaperTime = 10000;
    final private long moveAnimTime = 500;

    private LinearLayout fragmentHolder;
    private boolean isFragmentHolderOpen = false;
    private FragmentHolderState currentFragmentHolderState = FragmentHolderState.SETTINGS;

    private Animation leftIn, leftOut;

    ImageButton play, inventory, diary, settings;
    View settingsFragment, diaryFragment, inventoryFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFragmentHolder();
        setupButtonListeners();
        setupBGSwitcher();
    }

    private void setupFragmentHolder() {
        fragmentHolder = (LinearLayout) findViewById(R.id.fragment_holder);

        leftIn = AnimationUtils.loadAnimation(this, R.anim.move_left_in);
        leftOut = AnimationUtils.loadAnimation(this, R.anim.move_left_out);

        settingsFragment = findViewById(R.id.settings_fragment);
        diaryFragment = findViewById(R.id.diary_fragment);
        inventoryFragment = findViewById(R.id.inventory_fragment);

        settingsFragment.setEnabled(false);
        diaryFragment.setEnabled(false);
        inventoryFragment.setEnabled(false);
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
                handler.postDelayed(this, wallpaperTime);
            }
        }, wallpaperTime);
    }

    private void setupButtonListeners() {
        play = (ImageButton) findViewById(R.id.bt_play);
        inventory = (ImageButton) findViewById(R.id.bt_inventory);
        diary = (ImageButton) findViewById(R.id.bt_diary);
        settings = (ImageButton) findViewById(R.id.bt_settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragmentHolderState == FragmentHolderState.SETTINGS) {
                    triggerFragmentHolder();
                    return;
                }

                setFragmentHolderState(FragmentHolderState.SETTINGS);
            }
        });

        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragmentHolderState == FragmentHolderState.DIARY) {
                    triggerFragmentHolder();
                    return;
                }

                setFragmentHolderState(FragmentHolderState.DIARY);
            }
        });

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragmentHolderState == FragmentHolderState.INVENTORY) {
                    triggerFragmentHolder();
                    return;
                }

                setFragmentHolderState(FragmentHolderState.INVENTORY);
            }
        });
    }

    private void disableButtonsFor(long delay) {
        disableImageButtonFor(inventory, delay);
        disableImageButtonFor(diary, delay);
        disableImageButtonFor(settings, delay);
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
        disableButtonsFor(moveAnimTime);

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

    public void setFragmentHolderState(FragmentHolderState fragmentHolderState) {
        this.currentFragmentHolderState = fragmentHolderState;

        Log.d("holder", "" + fragmentHolderState);

        switch (fragmentHolderState) {
            case SETTINGS:
                settingsFragment.setEnabled(true);
                diaryFragment.setEnabled(false);
                inventoryFragment.setEnabled(false);
                break;
            case DIARY:
                settingsFragment.setEnabled(false);
                diaryFragment.setEnabled(true);
                inventoryFragment.setEnabled(false);
                break;
            case INVENTORY:
                settingsFragment.setEnabled(false);
                diaryFragment.setEnabled(false);
                inventoryFragment.setEnabled(true);
                break;
        }
    }


    private enum FragmentHolderState {
        SETTINGS, DIARY, INVENTORY
    }

}
