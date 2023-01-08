package edu.byuh.cis.cs203.bw_prefs.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.RectF;

import edu.byuh.cis.cs203.bw_prefs.R;
import edu.byuh.cis.cs203.bw_prefs.misc.Direction;
import edu.byuh.cis.cs203.bw_prefs.misc.Size;

public class Airplane extends Enemy {

    private static float minY, maxY;    //DIRTY HACK
    private Bitmap littleAirplaneRight, littleAirplaneLeft;
    private Bitmap mediumAirplaneRight, mediumAirplaneLeft;
    private Bitmap bigAirplaneRight, bigAirplaneLeft;
    private Bitmap kaboom;


    /**
     * The default constructor. Loads the correct plane picture for its size
     * @param res  the Android resources object
     * @param w the width of the screen, in pixels
     */
    public Airplane(Resources res, float w, float speed) {
        super(res, w, speed);
    }

    @Override
    protected Bitmap getImage() {
        switch (getSize()) {
            case SMALL:
                if (getDirection() == Direction.RIGHT_FACING) {
                    return littleAirplaneRight;
                } else {
                    return littleAirplaneLeft;
                }
            case MEDIUM:
                if (getDirection() == Direction.RIGHT_FACING) {
                    return mediumAirplaneRight;
                } else {
                    return mediumAirplaneLeft;
                }
            case LARGE:
            default:
                if (getDirection() == Direction.RIGHT_FACING) {
                    return bigAirplaneRight;
                } else {
                    return bigAirplaneLeft;
                }
        }
    }

    @Override
    protected void loadImages(Resources res) {
        littleAirplaneLeft = loadAndScale(res, R.drawable.little_airplane, 0.05);
        littleAirplaneRight = loadAndScale(res, R.drawable.little_airplane_flip, 0.05);
        mediumAirplaneLeft = loadAndScale(res, R.drawable.medium_airplane, 0.083);
        mediumAirplaneRight = loadAndScale(res, R.drawable.medium_airplane_flip, 0.083);
        bigAirplaneLeft = loadAndScale(res, R.drawable.big_airplane, 0.12);
        bigAirplaneRight = loadAndScale(res, R.drawable.big_airplane_flip, 0.12);
        kaboom = loadAndScale(res, R.drawable.airplane_explosion, 0.12);
    }


    /**
     * Setter method intended to be called by the View subclass, to set the upper and lower range
     * of this sprite's Y coordinates. The caller may pass either end of the range as either the
     * first or second parameter, and the method will put the smaller value in minY and the larger
     * in maxY.
     * @param y1 one end of the range
     * @param y2 the other end of the range
     */
    public static void setSkyLimits(float y1, float y2) {
        minY = Math.min(y1, y2);
        maxY = Math.max(y1, y2);
    }

    @Override
    public float getRandomHeight() {
        return (float)(minY + (maxY-bounds.height()-minY)*Math.random());
    }

    @Override
    public int getPointValue() {
        return switch (getSize()) {
            case SMALL -> 75;
            case MEDIUM -> 20;
            case LARGE -> 15;
        };
    }

    @Override
    public Bitmap explodingImage() {
        return kaboom;
    }

    @Override
    public boolean overlaps(Sprite s) {
        boolean result = super.overlaps(s);
        //for big airplanes, test against a smaller bounding box,
        //that fits the image more tightly
        if (result && getSize() == Size.LARGE) {
            RectF smallerBounds = new RectF(bounds);
            float height = bounds.height();
            smallerBounds.top = bounds.top + height*0.4f;
            smallerBounds.bottom = bounds.top + height*0.6f;
            result = RectF.intersects(smallerBounds, s.bounds);
        }
        return result;

    }

}
