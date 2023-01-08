package edu.byuh.cis.cs203.bw_prefs.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import edu.byuh.cis.cs203.bw_prefs.R;

public class SplashActivity extends Activity {

    private ImageView iv;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.splash);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_UP) {
            float x = m.getX();
            float y = m.getY();
            float w = iv.getWidth();
            float h = iv.getHeight();
            //RectF play = new RectF(0.4f*w, 0.43f*h, 0.59f*w, 0.6f*h);
            RectF about = new RectF(0, 0.8f * h, 0.1f * w, h);
            RectF prefs = new RectF(0, 0, 0.1f * w, 0.2f * h);
            if (about.contains(x, y)) {
                showAboutDialog();
            } else if (prefs.contains(x, y)) {
                showPrefsActivity();
            } else {
                launchGame();
            }
        }
        return true;
    }

    private void showPrefsActivity() {
        var i = new Intent(this, Prefs.class);
        startActivity(i);
    }

    private void launchGame() {
        var i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void showAboutDialog() {
        var ab = new AlertDialog.Builder(this);
        ab.setTitle(R.string.app_name)
                .setMessage(R.string.about)
                .setNeutralButton(R.string.ok, null);
        AlertDialog box = ab.create();
        box.show();

    }
}
