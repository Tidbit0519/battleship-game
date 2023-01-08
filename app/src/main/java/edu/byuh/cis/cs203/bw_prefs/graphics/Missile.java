package edu.byuh.cis.cs203.bw_prefs.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

import edu.byuh.cis.cs203.bw_prefs.misc.Direction;

public class Missile extends Sprite {

    private Paint missilePaint;
    private Direction dir;

    /**
     * public constructor for the Missile class
     * @param d is this a right-facing or a left-facing missile
     * @param w width of the screen, for scaling purposes
     * @param p Paint object used for drawing the missile
     */
    public Missile(Direction d, float w, Paint p) {
        super(w);
        dir = d;
        missilePaint = p;
        float newWidth = screenWidth * 0.04f;
        bounds.set(0,0,newWidth,newWidth);
        final float vel = bounds.width()*0.75f;
        if (dir == Direction.RIGHT_FACING) {
            velocity.set(vel, -vel);
        } else {
            velocity.set(-vel, -vel);
        }
    }

    @Override
    public void draw(Canvas c) {
        //Rather than draw an image, we just draw a diagonal line
        if (dir == Direction.RIGHT_FACING) {
            c.drawLine(bounds.left, bounds.bottom, bounds.right, bounds.top, missilePaint);
        } else {
            c.drawLine(bounds.left, bounds.top, bounds.right, bounds.bottom, missilePaint);
        }
    }
}


