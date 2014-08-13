package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


public class Block extends SurfaceView implements GestureDetector.OnGestureListener,
        SurfaceHolder.Callback, Runnable{
    int[][][] blocks = {
            {
                    {1,1},
                    {0,1},
                    {0,1}
            },
            {
                    {1,1},
                    {1,0},
                    {1,0}
            },
            {
                    {1,1},
                    {1,1}
            },
            {
                    {1,0},
                    {1,1},
                    {1,0}
            },
            {
                    {1,0},
                    {1,1},
                    {0,1}
            },
            {
                    {0,1},
                    {1,1},
                    {1,0}
            },
            {
                    {1},
                    {1},
                    {1},
                    {1}
            }
    };

    Random mRand = new Random(System.currentTimeMillis());

    int[][] block = blocks[mRand.nextInt(blocks.length)];
    int mapWidth  = 10;
    int mapHeight = 20;
    public int posx, posy;
    int[][] map = new int[mapHeight][];
    private final static int BLOCK_SIZE = 80;
    GestureDetector gestureDetector;
    static Thread mThread;
    private SurfaceHolder mHolder;
    static boolean mIsAttached;
    Canvas mCanvas = null;
    Paint mPaint = null;



    public Block(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context,this);
        mHolder = getHolder();
        mHolder.addCallback(this);
        Log.v("Constructor","true");
    }

    private void drawBlock(int[][] matrix, int offsetx, int offsety, int color) {
        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.getPaint().setColor(color);
//        mPaint.setColor(color);
//        mPaint.setStyle(Paint.Style.STROKE);
        mCanvas = getHolder().lockCanvas();
        mCanvas.drawColor(Color.BLACK);

        int h = matrix.length;
        int w = matrix[0].length;

        for (int y = 0; y < h; y ++) {
            for (int x = 0; x < w; x ++) {
                if (matrix[y][x] != 0) {
                    int px = (x + offsetx) * BLOCK_SIZE;
                    int py = (y + offsety) * BLOCK_SIZE;
                    rect.setBounds(px, py, px + BLOCK_SIZE, py + BLOCK_SIZE);
                    rect.draw(mCanvas);
//                    mCanvas.drawRect(px, py, px + BLOCK_SIZE, py + BLOCK_SIZE, mPaint);
                }
            }
        }
    }

    int[][] rotate(final int[][] block) {
        int[][] rotated = new int[block[0].length][];
        for (int x = 0; x < block[0].length; x ++) {
            rotated[x] = new int[block.length];
            for (int y = 0; y < block.length; y ++) {
                rotated[x][block.length - y - 1] = block[y][x];
            }
        }
        return rotated;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        int[][] newBlock = rotate(block);
            block = newBlock;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        //フリック方向の判定
        if (motionEvent.getX() < motionEvent2.getX()) {
            posx++;
        } else {
            posx--;
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.v("surfaceCreated","true");
        mPaint = new Paint();
        mIsAttached = true;
        mThread = new Thread(this);
        mThread.start();
    }



    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.v("surfaceDestroyed","true");
        mIsAttached = false;
        while (mThread.isAlive());

    }

    @Override
    public void run() {
        Log.v("run","true");
        while (mIsAttached) {
            drawBlock(block, posx, posy, Color.RED);
            getHolder().unlockCanvasAndPost(mCanvas);
        }
    }

    public static void finishLoop(){
        synchronized (mThread){
            mIsAttached = false;
        }
            try {
                mThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
    }
}
