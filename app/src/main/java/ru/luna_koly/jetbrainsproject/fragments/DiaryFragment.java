package ru.luna_koly.jetbrainsproject.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.luna_koly.jetbrainsproject.R;

/**
 * Created with love by iMac on 18.06.17.
 */

public class DiaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_menu_diary_fragment, container, false);
    }

}
