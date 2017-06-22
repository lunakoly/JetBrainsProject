package ru.luna_koly.jetbrainsproject.dialogs;

import android.os.Bundle;

import ru.luna_koly.jetbrainsproject.R;
import ru.luna_koly.jetbrainsproject.activityTemplates.NoTitleBarActivity;

public class DialogActivity extends NoTitleBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

}
