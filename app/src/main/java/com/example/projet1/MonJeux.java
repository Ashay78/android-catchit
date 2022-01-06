package com.example.projet1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projet1.Jeux;

import java.util.ArrayList;
import java.util.Random;

public class MonJeux extends View implements SensorEventListener
{
    SensorManager mgr;

    Jeux d;
    Paint image;
    float currentX,currentY;

    ArrayList<Point> pointsResultList;

    Point pointTarget;
    float rayon = 60;
    boolean existe = false ;
    int score = 0;
    private Bitmap bitmapJail;
    private Bitmap bitmapThief;
    private Bitmap bitmapBlood;
    private Bitmap bitmapCat;
    private Bitmap bitmapPolice;

    private String name;
    private String target;
    private String hunter;
    private String result;
    private String sound;

    private View mValue;
    private ImageView mImage;

    private TextView scoreTextView;

    private boolean isInvincible = false;

    private MediaPlayer soundPlayer = null;

    public MonJeux(Context context) {
        super(context);

    }

    public MonJeux(Context context , AttributeSet attrs) {
        super(context, attrs);
    }

    public void setParams(String name, String targer, String hunter, String result, String sound, TextView textView) {
        this.setFocusable(true);
        this.init();
        this.scoreTextView = textView;
        this.scoreTextView.setText("Score : " + score);

        this.name   = name;
        this.target = targer;
        this.hunter = hunter;
        this.result = result;
        this.sound  = sound;
        // soundPlayer = MediaPlayer.create(context, R.raw.dog);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        currentX = w / 2;
        currentY = h / 2;
    }

    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if(pointsResultList.size() == 0)
        {
            nouveau();
            existe = true;
        }
        else {
            for (int i = 0; i< pointsResultList.size(); i++)  {
                Point centre = pointsResultList.get(i);
                Bitmap result = this.getBitmap(this.result);
                canvas.drawBitmap(result, centre.x - ((float) result.getHeight()/2), centre.y - ((float) result.getWidth()/2), image);

            }
            Bitmap target = this.getBitmap(this.target);
            canvas.drawBitmap(target, pointTarget.x - ((float) target.getHeight()/2), pointTarget.y - ((float) target.getWidth()/2), image);

        }

        Bitmap hunter = this.getBitmap(this.hunter);
        canvas.drawBitmap(hunter, currentX - ((float) hunter.getHeight()/2), currentY - ((float) hunter.getWidth()/2), image);

    }

    /**
     * Set image
     * @param type
     * @return
     */
    public Bitmap getBitmap(String type) {
        switch (type) {
            case "jail":
                return this.bitmapJail;
            case "thief":
                return this.bitmapThief;
            case "blood":
                return this.bitmapBlood;
            case "cat":
                return this.bitmapCat;
            case "police":
                return this.bitmapPolice;
            default:
                return this.bitmapThief;
        }
    }

    /**
     * Create target point
     */
    private void nouveau(){
        Point p;
        Random rand = new Random();
        int randomX = rand.nextInt(getWidth());
        int randomY = rand.nextInt(getHeight());
        p = new Point((int) randomX, (int) randomY);
        pointsResultList.add(p);
        int randomX2 = rand.nextInt(getWidth());
        int randomY2 = rand.nextInt(getHeight());
        pointTarget = new Point((int) randomX2, (int) randomY2);
    }

    /**
     * Init image
     */
    private void init() {
        pointsResultList = new ArrayList<Point>();


        image = new Paint();
        bitmapJail = BitmapFactory.decodeResource(getResources(), R.drawable.jail);
        bitmapThief = BitmapFactory.decodeResource(getResources(), R.drawable.thief);
        bitmapBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood);
        bitmapCat = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        bitmapPolice = BitmapFactory.decodeResource(getResources(), R.drawable.police);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Movement of the player
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        this.moveHunter(-x*2,y*2);

    }

    /**
     * Check if player is on target or in result
     * @param x
     * @param y
     */
    private void moveHunter(float x, float y){
        Point p;
        Random rand = new Random();
        int randomX,randomY;

        currentX += (float) x;
        currentY += (float) y;

        if(currentX < 0) {
            currentX = 0;
        } else if ( currentX > getWidth() - 10 ){
            currentX = getWidth() - 10;
        }

        if(currentY < 0) {
            currentY = 0;
        } else if ( currentY > getHeight() - 10 ){
            currentY = getHeight() - 10;
        }

        // If touch the result
        if( touchResult((int) currentX,(int) currentY ) ){
            if (!this.isInvincible) {
                Activity activity = (Activity)getContext();
                Intent returnIntent = new Intent();
                String scoreText = Integer.toString(this.score);
                returnIntent.putExtra("result", scoreText);
                activity.setResult(Activity.RESULT_OK, returnIntent);
                activity.finish();
            }
        } else {
            this.isInvincible = false;
        }

        // If touche the target
        if( touchTarget( (int) currentX,(int) currentY ) ) {
            pointsResultList.add(this.pointTarget);

            randomX = rand.nextInt(getWidth());
            randomY = rand.nextInt(getHeight());
            this.pointTarget = new Point((int) randomX, (int) randomY);
            this.isInvincible = true;
//            soundPlayer.start();
            score++;
            this.scoreTextView.setText("Score : " + score);
        }

        this.invalidate();
    }

    /**
     * Check if target touch result
     * @param x
     * @param y
     * @return
     */
    private boolean touchResult(int x, int y) {
        boolean onResult = false;
        int index = 0;
        while( !onResult && ( index < pointsResultList.size() ) ) {
            if( distance( x,y, pointsResultList.get(index) ) <= rayon )
                onResult = true;
            else
                index++;
        }
        return onResult;
    }

    /**
     * check if user touch target
     * @param x
     * @param y
     * @return
     */
    private boolean touchTarget(int x, int y) {
        if(existe)
            return distance(x, y, pointTarget) <= rayon;

        return false;
    }

    /**
     * calculate the distance
     * @param x
     * @param y
     * @param p
     * @return
     */
    private int distance(int x, int y, Point p) {
        return (int)(Math.sqrt((int)((p.x - x)*(p.x-x))+((p.y-y)*(p.y-y))));
    }
}