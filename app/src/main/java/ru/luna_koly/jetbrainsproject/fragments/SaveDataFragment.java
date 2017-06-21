package ru.luna_koly.jetbrainsproject.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.luna_koly.jetbrainsproject.R;

/**
 * Created by user on 6/21/17.
 */

public class SaveDataFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.save_data_profile, container, false);
    }

}
