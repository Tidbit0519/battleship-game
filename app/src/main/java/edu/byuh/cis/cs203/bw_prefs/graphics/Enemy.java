package edu.byuh.cis.cs203.bw_prefs.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import edu.byuh.cis.cs203.bw_prefs.misc.Direction;
import edu.byuh.cis.cs203.bw_prefs.misc.Size;

public abstract class Enemy extends Sprite {
    private Size size;
    private Direction dir;
    private boolean exploding;
    private float speedFactor;

    /**
     * default constructor. Randomly selects a size (small, medium, large) for the sprite
     */
    public Enemy(Resources res, float w, float speed) {
        super(w);
        exploding = false;
        speedFactor = speed;
        loadImages(res);
        resetRandom();
    }

    @Override
    public void move() {
        super.move();

        //change the velocity 10% of the time
        if (Math.random() < 0.1) {
            velocity.x = getRandomVelocity();
        }

        //handle wraparound
        if (bounds.right < 0 || bounds.left > screenWidth) {
            resetRandom();
        }
    }

    /**
     * sets a new X velocity for the sprite.
     * I multiply it by signum to ensure that we preserve the direction
     * (left or right) of the velocity. Positive stays positive, negative
     * stays negative
     * @return a new random X velocity
     */
    private float getRandomVelocity() {
        return (float)(Math.random() * speedFactor * screenWidth * Math.signum(velocity.x));
    }

    protected abstract Bitmap getImage();

    /**
     * get the correct explosion image for this enemy
     * @return the explosion
     */
    protected abstract Bitmap explodingImage();

    /**
     * get the correct point value for the enemy that just got hit.
     * @return a number of points
     */
    public abstract int getPointValue();


    protected Size getSize() {
        return size;
    }

    protected Direction getDirection() {
        return dir;
    }

    protected void resetRandom() {
        double r1 = Math.random();
        double r2 = Math.random();
        if (r1 < 0.5) {
            dir = Direction.LEFT_FACING;
        } else {
            dir = Direction.RIGHT_FACING;
        }
        if (r2 < 0.3333) {
            size = Size.LARGE;
        } else if (r2 < 0.667) {
            size = Size.MEDIUM;
        } else {
            size = Size.SMALL;
        }
        image = getImage();
        bounds.set(0,0,image.getWidth(), image.getHeight());
        if (dir == Direction.LEFT_FACING) {
            velocity.x = -1;
            velocity.x = getRandomVelocity();
            bounds.offsetTo(screenWidth-1, getRandomHeight());
        } else {
            velocity.x = 1;
            velocity.x = getRandomVelocity();
            bounds.offsetTo(-image.getWidth()+1, getRandomHeight());
        }
    }

    /**
     * change enemy to exploding mode. Change image and velocity.
     */
    public void explode() {
        image = explodingImage();
        float newLeft = bounds.centerX() - image.getWidth()/2;
        float newRight = newLeft + image.getWidth();
        float newTop = bounds.centerY() - image.getHeight()/2;
        float newBottom = newTop + image.getHeight();
        bounds.set(newLeft, newTop, newRight, newBottom);
        velocity.set(0, 0);
        exploding = true;
    }


    /**
     * loads the correct image (type, size, and direction)
     * based on the subclass type and value of size/direction fields
     */
    protected abstract void loadImages(Resources res);

    /**
     * Randomly generate a Y position for an airplane/submarine.
     * @return a new Y position for the enemy craft
     */
    protected abstract float getRandomHeight();

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        if (exploding) {
            exploding = false;
            resetRandom();
        }
    }



}
