package edu.byuh.cis.cs203.bw_prefs.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import edu.byuh.cis.cs203.bw_prefs.R;
import edu.byuh.cis.cs203.bw_prefs.graphics.Airplane;
import edu.byuh.cis.cs203.bw_prefs.graphics.Battleship;
import edu.byuh.cis.cs203.bw_prefs.graphics.DepthCharge;
import edu.byuh.cis.cs203.bw_prefs.misc.Direction;
import edu.byuh.cis.cs203.bw_prefs.graphics.Missile;
import edu.byuh.cis.cs203.bw_prefs.graphics.Sprite;
import edu.byuh.cis.cs203.bw_prefs.graphics.Submarine;
import edu.byuh.cis.cs203.bw_prefs.misc.TickListener;
import edu.byuh.cis.cs203.bw_prefs.misc.Timer;

/**
 * It all happens here: the drawing, the tapping, the animation.
 */
public class GameView extends View implements TickListener {

    private Bitmap water;
    private Bitmap pop;
    private Battleship battleship;
    private List<Airplane> planes;
    private List<Submarine> subs;
    private boolean init;
    private Timer tim;
    private List<DepthCharge> bombs;
    private List<Missile> missiles;
    private float w,h;
    private Paint missilePaint;
    private boolean leftPop, rightPop;
    private int score;
    private Paint scorePaint, timePaint;
    private int timeLeft;
    private int timerCounter;
    private long bombTick;
    private MediaPlayer dcSound, leftSound, rightSound, airExplosion, waterExplosion;

    /**
     * Constructor for our View subclass. Loads all the images
     * @param context a reference to our main Activity class
     */
    public GameView(Context context) {
        super(context);
        pop = BitmapFactory.decodeResource(getResources(),R.drawable.star);
        water = BitmapFactory.decodeResource(getResources(), R.drawable.water);
        bombs = new ArrayList<>();
        missiles = new ArrayList<>();
        missilePaint = new Paint();
        missilePaint.setColor(Color.DKGRAY);
        missilePaint.setStyle(Paint.Style.STROKE);
        planes = new ArrayList<>();
        subs = new ArrayList<>();
        scorePaint = new Paint();
        scorePaint.setColor(Color.BLACK);
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        timePaint = new Paint(scorePaint);
        timePaint.setTextAlign(Paint.Align.RIGHT);
        init = false;
        leftPop = false;
        rightPop = false;
        dcSound = MediaPlayer.create(getContext(), R.raw.depth_charge);
        leftSound = MediaPlayer.create(getContext(), R.raw.left_gun);
        rightSound = MediaPlayer.create(getContext(), R.raw.right_gun);
        airExplosion = MediaPlayer.create(getContext(), R.raw.plane_explode);
        waterExplosion = MediaPlayer.create(getContext(), R.raw.sub_explode);
        reset();
    }

    private void reset() {
        timerCounter = 0;
        timeLeft = 180;
        score = 0;
    }

    /**
     * Scales, positions, and renders the scene
     * @param c the Canvas object, provided by system
     */
    @Override
    public void onDraw(Canvas c) {

        if (init == false) {
            init = true;
            w = getWidth();
            h = getHeight();
            scorePaint.setTextSize(h/15);
            timePaint.setTextSize(scorePaint.getTextSize());

            //scale the water
            final int waterWidth = (int)(w/50);
            water = Bitmap.createScaledBitmap(water, waterWidth,waterWidth, true);

            //scale the "pop"
            final int popWidth = (int)(w*0.03f);
            pop = Bitmap.createScaledBitmap(pop, popWidth, popWidth, true);

            //load and scale the battleship
            battleship = Battleship.getInstance(getResources(), w);
            missilePaint.setStrokeWidth(w*0.0025f);
            //position sprites
            final float battleshipX = w/2; //center the ship
            final float battleshipY = h/2-battleship.getHeight()*0.04f; //put the ship above the water line
            battleship.setLocation(battleshipX, battleshipY);

            //DIRTY HACK: inform Airplane class of acceptable upper/lower limits of flight
            final float battleshipTop = battleship.getTop()+battleship.getHeight()*0.4f;
            Airplane.setSkyLimits(0, battleshipTop);

            //DIRTY HACK: inform Submarine class of acceptable upper/lower limits of depth
            Submarine.setWaterDepth(h/2 + waterWidth*2, h);

            //load and scale the enemies
            float enemySpeed = Prefs.getPlaneSpeed(getContext());
            for (int i=0; i<Prefs.getNumPlanes(getContext()); i++) {
                planes.add(new Airplane(getResources(), w, enemySpeed));
            }
            enemySpeed = Prefs.getSubSpeed(getContext());
            for (int i=0; i<Prefs.getNumSubs(getContext()); i++) {
                subs.add(new Submarine(getResources(), w, enemySpeed));
            }

            //Once everything is in place, start the animation loop!
            tim = new Timer();
            //Using "method reference" syntax here, just for fun
            planes.forEach(tim::subscribe);
            subs.forEach(tim::subscribe);
            tim.subscribe(this);
        }

        //now draw everything
        c.drawColor(Color.WHITE);

        float waterX = 0;
        while (waterX < w) {
            c.drawBitmap(water, waterX, h/2, null);
            waterX += water.getWidth();
        }

        battleship.draw(c);
        planes.forEach(p -> p.draw(c));
        subs.forEach(s -> s.draw(c));
        missiles.forEach(m -> m.draw(c));
        bombs.forEach(d -> d.draw(c));

        if (leftPop) {
            final PointF popLocation = battleship.getLeftCannonPosition();
            c.drawBitmap(pop, popLocation.x-pop.getWidth(), popLocation.y-pop.getHeight(), null);
            leftPop = false;
        }
        if (rightPop) {
            final PointF popLocation = battleship.getRightCannonPosition();
            c.drawBitmap(pop, popLocation.x, popLocation.y-pop.getHeight(), null);
            rightPop = false;
        }
        final var scoreText = getResources().getString(R.string.score);
        c.drawText(scoreText + ": " + score, 5, h*0.6f, scorePaint);
        final String seconds = String.format("%02d", timeLeft % 60);
        final var timeText = getResources().getString(R.string.time);
        c.drawText(timeText + ": " + (timeLeft/60) + ":" + seconds, w-5, h*0.6f, timePaint);
        //c.drawText("MISSILES: " + missiles.size(), 5, h*0.7f, scorePaint);
        //c.drawText("DEPTHCHARGES: " + bombs.size(), 5, h*0.8f, scorePaint);
    }

    /**
     * launch depth charges and missiles based on the user's taps.
     * @param m an object encapsulating the (x,y) location of the user's tap
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
            //did the user tap the bottom half of the screen? Depth Charge!
            if (y > h/2) {
                if (Prefs.getRapidDC(getContext()) || !visibleBombs()) {
                    launchNewDepthCharge();
                }
            } else {
                //did the user tap the top half of the screen? missile!
                Missile miss = null;
                if (Prefs.getRapidGuns(getContext()) || !visibleMissiles()) {
                    if (x < getWidth() / 2) {
                        launchNewMissile(Direction.LEFT_FACING);
                    } else {
                        launchNewMissile(Direction.RIGHT_FACING);
                    }
                }
            }

            //clean up depth charges that go off-screen
            List<Sprite> doomed = bombs.stream().filter(dc -> dc.getTop() > getHeight()).collect(Collectors.toList());
            doomed.forEach(tim::unsubscribe);
            bombs.removeAll(doomed);

            //clean up missiles that go off-screen
            doomed = missiles.stream().filter(miss -> miss.getBottom() < 0).collect(Collectors.toList());
            doomed.forEach(tim::unsubscribe);
            missiles.removeAll(doomed);

        }
        return true;
    }

    private void launchNewDepthCharge() {
        var dc = new DepthCharge(getResources(), w);
        dc.setCentroid(getWidth()/2, getHeight()/2);
        bombs.add(dc);
        tim.subscribe(dc);
    }

    private void launchNewMissile(Direction d) {
        var miss = new Missile(d, w, missilePaint);
        if (d == Direction.LEFT_FACING) {
            miss.setBottomRight(battleship.getLeftCannonPosition());
            if (Prefs.getSoundFX(getContext())) {
                leftSound.start();
            }
            leftPop = true;
        } else {
            miss.setBottomLeft(battleship.getRightCannonPosition());
            if (Prefs.getSoundFX(getContext())) {
                rightSound.start();
            }
            rightPop = true;
        }
        missiles.add(miss);
        tim.subscribe(miss);
    }


    private void detectCollisions() {
        for (Submarine s : subs) {
            for (DepthCharge d : bombs) {
                if (d.overlaps(s)) {
                    s.explode();
                    score += s.getPointValue();
                    if (Prefs.getSoundFX(getContext())) {
                        waterExplosion.start();
                    }
                    //hide the depth charge off-screen; it will get cleaned
                    //up at the next touch event.
                    d.setLocation(0,getHeight());
                }
            }
        }

        for (Airplane p : planes) {
            for (Missile m : missiles) {
                if (p.overlaps(m)) {
                    p.explode();
                    score += p.getPointValue();
                    if (Prefs.getSoundFX(getContext())) {
                        airExplosion.start();
                    }
                    //hide the missile charge off-screen; it will get cleaned
                    //up at the next touch event.
                    m.setLocation(0,-getHeight());
                }
            }
        }
    }

    @Override
    public void tick() {
        invalidate();
        detectCollisions();
        timerCounter++;
        if (timerCounter >= 10) {
            timeLeft--;
            timerCounter = 0;
        }
        if (timeLeft <= 0) {
            endgame();
        }
        if (bombTick % 10 == 0 && visibleBombs()) {
            if (Prefs.getSoundFX(getContext())) {
                dcSound.start();
            }
        }
        bombTick++;
    }

    private boolean visibleBombs() {
        boolean result = false;
        for (DepthCharge d : bombs) {
            if (d.getTop() < h) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean visibleMissiles() {
        boolean result = false;
        for (Missile d : missiles) {
            if (d.getBottom() > 0) {
                result = true;
                break;
            }
        }
        return result;
    }


    private void endgame() {
        tim.pause();
        String message = "";
        int oldScore = 0;
        //attempt to load the old score
        final String scoreFile = "highscore.txt";
        //try with resources syntax
        try(var is = getContext().openFileInput(scoreFile)){
            Scanner s = new Scanner(is);
            oldScore = s.nextInt();
        } catch (IOException e) {
            //do nothing, just use 0 as old score
        }
        if (oldScore < score) {
            message = getResources().getString(R.string.congrats);
            //now, save the new score
            //try with resources syntax
            try(var os = getContext().openFileOutput(scoreFile, Context.MODE_PRIVATE)){
                os.write((""+score).getBytes());
            } catch (IOException e) {
                //do nothing. There's nothing we could do anyway.
            }
        } else {
            message = getResources().getString(R.string.consolation, oldScore);
        }

        message += " " + getResources().getString(R.string.play_again);

        //Now, prep the dialog box
        final var ab = new AlertDialog.Builder(getContext());
        ab.setTitle(R.string.game_over)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int w) {
                        tim.resume();
                        reset();
                    }
                })
                .setNegativeButton(R.string.no, (d, w) -> ((Activity)getContext()).finish());
        AlertDialog box = ab.create();
        box.show();
    }

    /**
     * Turn off the timer, and un-load all sound effects.
     * To be called by MainActivity.onDestroy()
     */
    void shutdown() {
        tim.stop();
        dcSound.release();
        leftSound.release();
        rightSound.release();
        airExplosion.release();
        waterExplosion.release();
    }

    public void gotoBackground() {
        if (tim != null) {
            tim.pause();
        }
    }

    public void gotoForeground() {
        if (tim != null) {
            tim.resume();
        }
    }

}


