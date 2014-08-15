package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


public class Block extends SurfaceView implements GestureDetector.OnGestureListener,
        SurfaceHolder.Callback, Runnable, GestureDetector.OnDoubleTapListener {
    int[][][] blocks = {
            {
                    {1, 1},
                    {0, 1},
                    {0, 1}
            },
            {
                    {1, 1},
                    {1, 0},
                    {1, 0}
            },
            {
                    {1, 1},
                    {1, 1}
            },
            {
                    {1, 0},
                    {1, 1},
                    {1, 0}
            },
            {
                    {1, 0},
                    {1, 1},
                    {0, 1}
            },
            {
                    {0, 1},
                    {1, 1},
                    {1, 0}
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
    int mapWidth = 12;
    int mapHeight = 21;
    public int posx = mapWidth / 2, posy;
    int[][] map = new int[mapHeight][];
    private final static int BLOCK_SIZE = 53;
    GestureDetector gestureDetector;
    static Thread mThread;
    private SurfaceHolder mHolder;
    static boolean mIsAttached;
    Canvas mCanvas = null;
    static int fallVelocity;
    static int frame;



    public Block(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, this);
        mHolder = getHolder();
        mHolder.addCallback(this);
        initGame();
    }

    private void drawMatrix(int[][] matrix, int offsetx, int offsety, int color) {
        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.getPaint().setColor(color);

        int h = matrix.length;
        int w = matrix[0].length;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (matrix[y][x] != 0) {
                    int px = (x + offsetx) * BLOCK_SIZE;
                    int py = (y + offsety) * BLOCK_SIZE;
                    rect.setBounds(px, py, px + BLOCK_SIZE, py + BLOCK_SIZE);
                    rect.draw(mCanvas);
                }
            }
        }
    }

    int[][] rotate(final int[][] block) {
        int[][] rotated = new int[block[0].length][];
        for (int x = 0; x < block[0].length; x++) {
            rotated[x] = new int[block.length];
            for (int y = 0; y < block.length; y++) {
                rotated[x][block.length - y - 1] = block[y][x];
            }
        }
        return rotated;
    }

    boolean check(int[][] block, int offsetx, int offsety) {
        if (offsetx < 0 || offsety < 0 ||
                mapHeight - 1 < offsety + block.length ||
                mapWidth - 1 < offsetx + block[0].length) {
            Log.v("check", "true");
            return false;
        }
        for (int y = 0; y < block.length; y++) {
            for (int x = 0; x < block[y].length; x++) {
                if (block[y][x] != 0 && map[y + offsety][x + offsetx] != 0) {
                    return false;
                }
            }
        }
        return true;
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
        if (check(newBlock, posx, posy)) {
            block = newBlock;
        }
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
            if (check(block, posx + 1, posy)) {
                posx++;
            }
        } else {
            if (check(block, posx - 1, posy)) {
                posx--;
            }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        frame = 0;
        mIsAttached = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mIsAttached = false;
        while (mThread.isAlive()) ;

    }

    @Override
    public void run() {
        while (mIsAttached) {
            mCanvas = getHolder().lockCanvas();
            mCanvas.drawColor(Color.BLACK);
            drawMatrix(block, posx, posy, Color.RED);
            drawMatrix(map, 0, 0, Color.GRAY);
            if (frame % fallVelocity == 0) {
                Log.v("frame", Integer.toString(frame));
                dropBlock();
            }
            frame++;
            getHolder().unlockCanvasAndPost(mCanvas);
        }
    }

    public void dropBlock() {
        if (check(block, posx, posy + 1)) {
            posy++;
        } else {
            posx = mapWidth / 2;
            posy = 0;
            block = blocks[mRand.nextInt(blocks.length)];
        }
    }

    public static void finishLoop() {
        synchronized (mThread) {
            mIsAttached = false;
        }
        try {
            mThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void setFallVelocity(int difficulty) {
        fallVelocity = difficulty;
    }

    public void initGame() {
        for (int y = 0; y < mapHeight; y++) {
            map[y] = new int[mapWidth];
            for (int x = 0; x < mapWidth; x++) {
                //マップの外枠に色を塗る
                if (y == mapHeight - 1 || x == mapWidth - 1
                        || x == 0) {
                    map[y][x] = 1;
                }
            }
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        int y = posy;
        while (check(block, posx, y)) {
            y++;
        }
        if (y > 0) {
            posy = y - 1;
        }
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
}
