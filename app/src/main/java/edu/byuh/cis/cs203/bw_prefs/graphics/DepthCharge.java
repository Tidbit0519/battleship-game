package edu.byuh.cis.cs203.bw_prefs.graphics;

import android.content.res.Resources;
import android.graphics.PointF;
import edu.byuh.cis.cs203.bw_prefs.R;

public class DepthCharge extends Sprite {

    /**
     * public constructor for the depth charge class
     * @param res the Android "Resources" object, used for loading images
     * @param w width of the screen, for scaling purposes
     */
    public DepthCharge(Resources res, float w) {
        super(w);
        image = loadAndScale(res, R.drawable.depth_charge, 0.025);
        velocity = new PointF(0, w/100);
    }

//    @Override
//    protected float relativeWidth() {
//        return 0.025f;
//    }

}

