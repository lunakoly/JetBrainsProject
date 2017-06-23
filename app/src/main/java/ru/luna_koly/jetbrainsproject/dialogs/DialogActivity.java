package ru.luna_koly.jetbrainsproject.dialogs;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import ru.luna_koly.jetbrainsproject.R;
import ru.luna_koly.jetbrainsproject.activityTemplates.NoTitleBarActivity;

public class DialogActivity extends NoTitleBarActivity {
    private static Dialog dialog = null;
    private static TextView logs = null;
    private static LinearLayout buttonHandler = null;

    private static final int[] bgs = {
            R.drawable.small_wallpaper_2,
            R.drawable.small_wallpaper_3,
            R.drawable.small_wallpaper_4,
            R.drawable.small_wallpaper_5,
            R.drawable.small_wallpaper_6
    };


    public static void setDialog(Dialog dialog) {
        DialogActivity.dialog = dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        logs = (TextView) findViewById(R.id.logs);
        buttonHandler = (LinearLayout) findViewById(R.id.button_handler);
        ImageView bg = (ImageView) findViewById(R.id.background);

        int i = new Random().nextInt(bgs.length);
        bg.setImageResource(bgs[i]);

        if (dialog != null) runDialog(dialog);
    }

    private void rep(String who, String text) {
        log(who + ":   " + text);
    }

    private void rep(Replica r) {
        rep(r.person, r.text);
    }

    private void runDialog(Dialog dialog) {
        Replica start = dialog.get("0");
        runReplica("0");
    }

    private void log(String s) {
        if (logs != null) logs.setText(logs.getText() + "\n" + s);
    }

    private void runReplica(String s) {
        Replica r = dialog.get(s);

        if (r != null) {
            if (r.person.equals("Charly"))
                addButton(r);
            else {
                rep(r);
                clearButtons();
                if (r.nextReplicas.length > 0)
                    for (String replica : r.nextReplicas)
                        runReplica(replica);
                else {
                    addExitButton();
                }
            }
        }
    }

    private void addExitButton() {
        Button button = getButtonObject();
        button.setText("[ End dialog ]");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (buttonHandler != null) {
            buttonHandler.addView(button);
        }
    }

    private void clearButtons() {
        if (buttonHandler != null) {
            buttonHandler.removeAllViews();
        }
    }

    private void addButton(final Replica r) {
        Button button = getButtonObject();
        button.setText(r.text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r.nextReplicas.length > 0) {
                    rep(r);
                    clearButtons();
                    for (String s : r.nextReplicas) {
                        runReplica(s);
                    }
                } else {
                    finish();
                }
            }
        });

        if (buttonHandler != null) {
            buttonHandler.addView(button);
        }
    }

    private Button getButtonObject() {
        Button button = new Button(this, null, R.style.text_white);
        button.setTextAppearance(this, R.style.text_white);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 3, 0, 0);
        button.setGravity(Gravity.CENTER);
        button.setPadding(10, 10, 10, 10);
        button.setLayoutParams(layoutParams);
        button.setBackgroundResource(R.color.colorPrimary);
        return button;
    }

}
