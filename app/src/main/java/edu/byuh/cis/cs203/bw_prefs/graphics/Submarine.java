package edu.byuh.cis.cs203.bw_prefs.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;

import edu.byuh.cis.cs203.bw_prefs.R;
import edu.byuh.cis.cs203.bw_prefs.misc.Direction;

public class Submarine extends Enemy {

    private static float minY, maxY;    //DIRTY HACK
    private Bitmap littleSubRight, littleSubLeft;
    private Bitmap mediumSubRight, mediumSubLeft;
    private Bitmap bigSubRight, bigSubLeft;
    private Bitmap kaboom;

    /**
     * The default constructor. Loads the correct sub picture for its size
     * @param res  the Android resources object
     * @param w the width of the screen, in pixels
     */
    public Submarine(Resources res, float w, float speed) {
        super(res, w, speed);
    }

    @Override
    protected Bitmap getImage() {
        switch (getSize()) {
            case SMALL:
                if (getDirection() == Direction.RIGHT_FACING) {
                    return littleSubRight;
                } else {
                    return littleSubLeft;
                }
            case MEDIUM:
                if (getDirection() == Direction.RIGHT_FACING) {
                    return mediumSubRight;
                } else {
                    return mediumSubLeft;
                }
            case LARGE:
            default:
                if (getDirection() == Direction.RIGHT_FACING) {
                    return bigSubRight;
                } else {
                    return bigSubLeft;
                }
        }
    }

    @Override
    protected Bitmap explodingImage() {
        return kaboom;
    }

    @Override
    public int getPointValue() {
        switch (getSize()) {
            case SMALL:
                return 150;
            case MEDIUM:
                return 40;
            case LARGE:
            default:
                return 20;
        }
    }

    @Override
    protected void loadImages(Resources res) {
        littleSubLeft = loadAndScale(res, R.drawable.little_submarine_flip, 0.05);
        littleSubRight = loadAndScale(res, R.drawable.little_submarine, 0.05);
        mediumSubLeft = loadAndScale(res, R.drawable.medium_submarine_flip, 0.083);
        mediumSubRight = loadAndScale(res, R.drawable.medium_submarine, 0.083);
        bigSubLeft = loadAndScale(res, R.drawable.big_submarine_flip, 0.1);
        bigSubRight = loadAndScale(res, R.drawable.big_submarine, 0.1);
        kaboom = loadAndScale(res, R.drawable.submarine_explosion, 0.1);
    }

    @Override
    public float getRandomHeight() {
        return (float)(minY + (maxY-bounds.height()-minY)*Math.random());
    }

    /**
     * Setter method intended to be called by the View subclass, to set the upper and lower range
     * of this sprite's Y coordinates. The caller may pass either end of the range as either the
     * first or second parameter, and the method will put the smaller value in minY and the larger
     * in maxY.
     * @param y1 one end of the range
     * @param y2 the other end of the range
     */
    public static void setWaterDepth(float y1, float y2) {
        minY = Math.min(y1, y2);
        maxY = Math.max(y1, y2);
    }
}
