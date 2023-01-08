package edu.byuh.cis.cs203.bw_prefs.ui;

import android.app.Activity;
import android.os.Bundle;

import edu.byuh.cis.cs203.bw_prefs.R;

public class MainActivity extends Activity {

    private GameView gv;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        gv = new GameView(this);
        setContentView(gv);
    }

    @Override
    public void onPause() {
        super.onPause();
        gv.gotoBackground();
    }

    @Override
    public void onResume() {
        super.onResume();
        gv.gotoForeground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gv.shutdown();
    }
}