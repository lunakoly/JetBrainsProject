package ru.luna_koly.jetbrainsproject.activityTemplates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.luna_koly.jetbrainsproject.R;

/**
 * Created by user on 6/16/17.
 */

public class NoTitleBarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide title bar
        setTheme(R.style.AppTheme_NoActionBar);
    }
}
