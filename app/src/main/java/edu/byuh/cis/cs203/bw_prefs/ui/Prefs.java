package edu.byuh.cis.cs203.bw_prefs.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import edu.byuh.cis.cs203.bw_prefs.R;

public class Prefs extends AppCompatActivity {

    private static final String OPT_SOUND = "soundfx";
    private static final String OPT_RAPID_GUNS = "rapid_guns";
    private static final String OPT_RAPID_DC = "rapid_dc";
    private static final String OPT_MINUTES = "minutes";
    private static final String OPT_NUM_SUBS = "num_subs";
    private static final String OPT_NUM_PLANES = "num_planes";
    private static final String OPT_PLANE_SPEED = "plane_speed";
    private static final String OPT_SUB_SPEED = "sub_speed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            Context cx = getPreferenceManager().getContext();
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(cx);

            var speed = new SwitchPreference(cx);
            speed.setTitle(R.string.soundfx);
            speed.setSummaryOn(R.string.soundfx_on);
            speed.setSummaryOff(R.string.soundfx_off);
            speed.setChecked(true);
            speed.setKey(OPT_SOUND);
            screen.addPreference(speed);

            var rapidGuns = new SwitchPreference(cx);
            rapidGuns.setTitle(R.string.rapid_cannons);
            rapidGuns.setSummaryOn(R.string.rapid_cannons_on);
            rapidGuns.setSummaryOff(R.string.rapid_cannons_off);
            rapidGuns.setChecked(true);
            rapidGuns.setKey(OPT_RAPID_GUNS);
            screen.addPreference(rapidGuns);

            var rapidDC = new SwitchPreference(cx);
            rapidDC.setTitle(R.string.rapid_dc);
            rapidDC.setSummaryOn(R.string.rapid_dc_on);
            rapidDC.setSummaryOff(R.string.rapid_dc_off);
            rapidDC.setChecked(true);
            rapidDC.setKey(OPT_RAPID_DC);
            screen.addPreference(rapidDC);

            var numSubs = new ListPreference(cx);
            numSubs.setTitle(R.string.howmany_subs);
            numSubs.setSummary(R.string.howmany_subs_summary);
            String[] values10 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
            numSubs.setEntries(R.array.one_to_ten);
            numSubs.setEntryValues(values10);
            numSubs.setKey(OPT_NUM_SUBS);
            screen.addPreference(numSubs);

            var numPlanes = new ListPreference(cx);
            numPlanes.setTitle(R.string.howmany_planes);
            numPlanes.setSummary(R.string.howmany_planes_summary);
            numPlanes.setEntries(R.array.one_to_ten);
            numPlanes.setEntryValues(values10);
            numPlanes.setKey(OPT_NUM_PLANES);
            screen.addPreference(numPlanes);

            var subSpeed = new ListPreference(cx);
            subSpeed.setTitle(R.string.sub_speed);
            subSpeed.setSummary(R.string.sub_speed_summary);
            String[] speedValues = {"0.01", "0.00625", "0.001"};
            subSpeed.setEntries(R.array.speed_entries);
            subSpeed.setEntryValues(speedValues);
            subSpeed.setKey(OPT_SUB_SPEED);
            screen.addPreference(subSpeed);

            var planeSpeed = new ListPreference(cx);
            planeSpeed.setTitle(R.string.plane_speed);
            planeSpeed.setSummary(R.string.plane_speed_summary);
            planeSpeed.setEntries(R.array.speed_entries);
            planeSpeed.setEntryValues(speedValues);
            planeSpeed.setKey(OPT_PLANE_SPEED);
            screen.addPreference(planeSpeed);

            setPreferenceScreen(screen);
        }
    }

    //Get the current value of the sound option
    public static boolean getSoundFX(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(OPT_SOUND, true);
    }

    public static boolean getRapidGuns(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(OPT_RAPID_GUNS, true);
    }

    public static boolean getRapidDC(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(OPT_RAPID_DC, false);
    }

//	public static int getGameLength(Context context) {
//		String tmp = PreferenceManager.getDefaultSharedPreferences(context)
//				.getString(OPT_MINUTES, "180");
//		return Integer.parseInt(tmp);
//	}

    public static int getNumPlanes(Context context) {
        String tmp = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OPT_NUM_PLANES, "3");
        return Integer.parseInt(tmp);
    }

    public static int getNumSubs(Context context) {
        String tmp = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OPT_NUM_SUBS, "3");
        return Integer.parseInt(tmp);
    }

    public static float getPlaneSpeed(Context context) {
        String tmp = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OPT_PLANE_SPEED, "0.00625");
        return Float.parseFloat(tmp);
    }
    public static float getSubSpeed(Context context) {
        String tmp = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(OPT_SUB_SPEED, "0.00625");
        return Float.parseFloat(tmp);
    }

}