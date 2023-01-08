package edu.byuh.cis.cs203.bw_prefs.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byuh.cis.cs203.bw_prefs.misc.TickListener;

public abstract class Sprite  implements TickListener {
    protected Bitmap image;
    protected RectF bounds;
    protected PointF velocity;
    protected float screenWidth;

    /**
     * default constructor.
     */
    public Sprite(float w) {
        bounds = new RectF();
        velocity = new PointF();
        screenWidth = w;
    }

    /**
     * set the sprite's center to (x,y)
     * @param x the new X coordinate
     * @param y the new Y coordinate
     */
    public void setLocation(float x, float y) {
        bounds.offsetTo(x-bounds.width()/2, y-bounds.height()/2);
    }

    /**
     * Move the sprite by its velocity
     */
    public void move() {
        bounds.offset(velocity.x, velocity.y);
    }

    /**
     * Render the sprite at its pre-set location
     * @param c the Android Canvas object
     */
    public void draw(Canvas c) {
        c.drawBitmap(image, bounds.left,  bounds.top, null);
    }

    /**
     * helper method to load and scale images in one single "factory" method
     * @param res the Android Resources object, required by underlying API
     * @param id the ID number of the image
     * @param relW a fraction, representing how wide the new image should be, relative to the screen width
     * @return the image, scaled appropriately
     */
    protected Bitmap loadAndScale(Resources res, int id, double relW) {
        final Bitmap bmp = BitmapFactory.decodeResource(res, id);
        final int relativeWidth = (int)(relW * screenWidth);
        return Bitmap.createScaledBitmap(bmp, relativeWidth, relativeWidth, true);
    }

    /**
     * helper method to find out how tall the sprite is
     * @return the height of the sprite, in piexls
     */
    public float getHeight() {
        return bounds.height();
    }

    /**
     * helper method to return the top Y coordinate of the sprite
     * @return the top Y coordinate of the sprite
     */
    public float getTop() {
        return bounds.top;
    }

    /**
     * helper method to return the lower Y coordinate of the sprite
     * @return the lower Y coordinate of the sprite
     */
    public float getBottom() {
        return bounds.bottom;
    }

    /**
     * helper method to find out how wide the sprite is
     * @return the width of the sprite, in pixels
     */
    public float getWidth() {
        return bounds.width();
    }

    /**
     * moves the sprite's center point to (x,y)
     * @param x the new center X
     * @param y the new center Y
     */
    public void setCentroid(float x, float y) {
        bounds.offsetTo(x-bounds.width()/2, y-bounds.height()/2);
    }

    /**
     * set the sprite's bottom-left corner to (x,y)
     * @param p the new point
     */
    public void setBottomLeft(PointF p) {
        bounds.offsetTo(p.x, p.y-bounds.height());
    }

    /**
     * set the sprite's bottom-right corner to (x,y)
     * @param p the new point
     */
    public void setBottomRight(PointF p) {
        bounds.offsetTo(p.x-bounds.width(), p.y-bounds.height());
    }

    /**
     * Determines whether "this" sprite's bounding box intersects another sprite's bounding box.
     * @param s the sprite to compare against this one.
     * @return true if they overlap; false if not.
     */
    public boolean overlaps(Sprite s) {
        return RectF.intersects(s.bounds, this.bounds);
    }

    @Override
    public void tick() {
        move();
    }


}

