package ru.luna_koly.jetbrainsproject;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created with love by luna_koly on 19.06.17.
 */

public class FileLoader {
    private static final String TAG = "file_loader";

    private static String readStream(InputStream is) {
        Scanner scn = new Scanner(is);
        String out = "";

        while (scn.hasNextLine()) {
            out += scn.nextLine();
        }

        return out;
    }

    private static InputStream streamFile(Context context, String path) {
        try {
            return context.getAssets().open(path);
        } catch (IOException e) {
            Log.d(TAG, "Failed loading file at \'" + path + "\'");
            e.printStackTrace();
        }

        return null;
    }

    public static String readFile(Context context, String path) {
        return readStream(streamFile(context, path));
    }

}
