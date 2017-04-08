package com.dc.p6_dc;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import javax.xml.transform.Result;

/**
 * Created by danet on 3/14/2017.
 */

public class AdventureView extends View {
    Resources resources;
    private Paint mPaint;
    Bitmap forestB, mountainB, plainsB, characterB, outB, treasureB, waterB;
    char map[][];
    private PointF mPlayerCoords;
    private int mBitmapWidth, mScreenOffsetX, mScreenOffsetY, characterX, characterY, mScreenWidth, mScreenHeight;
    Rect topRect, leftRec, bottomRect;
    AdventureView aView;
    boolean imagesLoaded;

    // Used when inflating the view from code
    public AdventureView(Context context) {
        super(context);
        setup();
    }

    // Used when inflating the view from XML
    public AdventureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }


    //TODO: THIS NEEDS TO HAPPEN IN THE BACKGROUND
    private void setup() {
        resources = getResources();
        mPaint = new Paint();

        map = new char[][]{
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'w', 'w', 'w', '.', '.', '.', '.', '.', '.', '.'},
                {'~', '~', '~', '~', '~', '~', '~', '~', '~', '~', 'w', 'w', 'w', '.', '.', '.', '.', '.', '.', '.'},
                {'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'M', 'M', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 't', 'f', 'f', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'~', '~', '~', '~', '~', '~', '~', '~', '~', '~', 'f', 'f', 'f', '.', '.', '.', '.', '.', '.', '.'},
                {'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'f', 'f', 'f', 'f', '.', '.', '.', '.', '.', '.'},
                {'~', '~', '~', '~', '~', '~', '~', '~', '~', '~', 'f', 'f', 'f', 'f', 'f', '.', '.', '.', '.', '.'},
                {'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', '.', '.', '.', '.'},
                {'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', 'f', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 't', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                {'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}
        };

        mPlayerCoords = new PointF(map.length / 2, map.length / 2);
        mScreenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        mScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        topRect = new Rect(0, 0, mScreenWidth, mScreenHeight / 4);
        bottomRect = new Rect(0, (int) (mScreenHeight * .75), mScreenWidth, mScreenHeight);
        leftRec = new Rect(0, (int) (mScreenHeight * .25), (int) (mScreenWidth * .5), (int) (mScreenHeight * .75));
        new ImageLoaderTask().execute();
    }

    public boolean onTouchEvent(MotionEvent e) {
        if (imagesLoaded) {
            PointF current = new PointF(e.getX(), e.getY());
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                if (topRect.contains((int) current.x, (int) current.y)) {
                    upTouched();
                } else if (bottomRect.contains((int) current.x, (int) current.y)) {
                    downTouched();
                } else if (leftRec.contains((int) current.x, (int) current.y)) {
                    leftTouched();
                } else {
                    rightTouched();
                }
                invalidate();
            }
        }

        return true;
    }

    private void upTouched() {
        //if player is at top of map
        if (characterY == 0) {
            if (mScreenOffsetY <= 0) {
                moveMapDown();
            }
            if (mScreenOffsetY > 0) {
                characterY = mBitmapWidth;
            }
        } else if (mScreenOffsetY < 0 || characterY >= mBitmapWidth * 2) {
            moveUp();
        }
    }

    private void downTouched() {
        //if player is at bottom of map
        int limit = -(mBitmapWidth * (map.length - (int) (aView.getHeight() / mBitmapWidth)));
        if (characterY + mBitmapWidth >= aView.getBottom() - mBitmapWidth) {

            if (mScreenOffsetY >= limit) {
                moveMapUp();
            }
            if (mScreenOffsetY < limit) {
                characterY = mBitmapWidth * (int) (aView.getHeight() / mBitmapWidth) - mBitmapWidth * 2;
            }
        } else if (mScreenOffsetY > limit || characterY < mBitmapWidth * (int) (aView.getHeight() / mBitmapWidth) - mBitmapWidth * 2) {
            moveDown();
        }
    }

    private void rightTouched() {
        //if player is at right of map
        int limit = -(mBitmapWidth * (map[0].length - (int) (aView.getWidth() / mBitmapWidth)));
        if (characterX + mBitmapWidth >= aView.getRight() - mBitmapWidth) {

            if (mScreenOffsetX >= limit) {
                moveMapLeft();
            }
            if (mScreenOffsetX < limit) {
                characterX = mBitmapWidth * (int) (aView.getWidth() / mBitmapWidth) - mBitmapWidth * 2;
            }
        } else if (mScreenOffsetX > limit || characterX < mBitmapWidth * (int) (aView.getWidth() / mBitmapWidth) - mBitmapWidth * 2) {
            moveRight();
        }
    }

    private void leftTouched() {
        //if player is at left of map
        if (characterX == 0) {
            if (mScreenOffsetX <= 0) {
                moveMapRight();
            }
            if (mScreenOffsetX > 0) {
                characterX = mBitmapWidth;
            }
        } else if (mScreenOffsetX < 0 || characterX >= mBitmapWidth * 2) {
            moveLeft();
        }
    }

    private void moveUp() {
        characterY -= mBitmapWidth;
    }

    private void moveDown() {
        characterY += mBitmapWidth;
    }

    private void moveLeft() {
        characterX -= mBitmapWidth;
    }

    private void moveRight() {
        characterX += mBitmapWidth;
    }

    private void moveMapDown() {
        mScreenOffsetY += mBitmapWidth;
    }

    private void moveMapUp() {
        mScreenOffsetY -= mBitmapWidth;
    }

    private void moveMapRight() {
        mScreenOffsetX += mBitmapWidth;
    }

    private void moveMapLeft() {
        mScreenOffsetX -= mBitmapWidth;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (imagesLoaded) {
            aView = (AdventureView) findViewById(R.id.adventureView);
            Rect viewRect = new Rect(aView.getLeft(), aView.getTop(), aView.getLeft() + aView.getWidth(), aView.getTop() + aView.getHeight());
            // Fill the background
            canvas.drawPaint(mPaint);

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    int x = j * mBitmapWidth + mScreenOffsetX;
                    int y = i * mBitmapWidth + mScreenOffsetY;
                    switch (map[i][j]) {
                        case '.':
                            canvas.drawBitmap(plainsB, x, y, null);
                            break;
                        case 'f':
                            canvas.drawBitmap(forestB, x, y, null);
                            break;
                        case '~':
                            canvas.drawBitmap(waterB, x, y, null);
                            break;
                        case 't':
                            canvas.drawBitmap(treasureB, x, y, null);
                            break;
                        default:
                            canvas.drawBitmap(mountainB, x, y, null);
                            break;
                    }
                }

            }
            canvas.drawBitmap(characterB, characterX, characterY, null);
        }
    }

    private class ImageLoaderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            forestB = BitmapFactory.decodeResource(resources, R.drawable.forest);
            mountainB = BitmapFactory.decodeResource(resources, R.drawable.mountain);
            plainsB = BitmapFactory.decodeResource(resources, R.drawable.plain);
            characterB = BitmapFactory.decodeResource(resources, R.drawable.person);
            outB = BitmapFactory.decodeResource(resources, R.drawable.out);
            treasureB = BitmapFactory.decodeResource(resources, R.drawable.treasure);
            waterB = BitmapFactory.decodeResource(resources, R.drawable.water);
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            imagesLoaded = true;
            mBitmapWidth = plainsB.getWidth();
            mScreenOffsetX = -mBitmapWidth * 5;
            mScreenOffsetY = -mBitmapWidth * 5;
            characterX = mBitmapWidth * 5;
            characterY = mBitmapWidth * 5;
        }



    }
}