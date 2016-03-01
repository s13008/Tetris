package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;
import android.media.SoundPool;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Block extends SurfaceView implements GestureDetector.OnGestureListener,
        SurfaceHolder.Callback, Runnable, GestureDetector.OnDoubleTapListener {

    //SE, BGMの実装
    private SoundPool se;
    private MediaPlayer bgm;

    private int bgmSoundId;
    private int seSoundId;


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

    private int[][] block = blocks[mRand.nextInt(blocks.length)];
    private int[][] next;
    int[][] start;
    private int mapWidth = 12;
    private int mapHeight = 21;
    private int posx = (mapWidth / 2) - 1, posy;
    private int[][] blockMap = new int[mapHeight][];
    public final static int BLOCK_WIDTH = 45;
    public final static int BLOCK_HEIGHT = 53;
    private GestureDetector gestureDetector;
    protected static Thread mThread;
    private SurfaceHolder mHolder;
    public static boolean mIsAttached;
    private Canvas mCanvas = null;
    public static int fallVelocity;
    public static int frame;
    private static final int ORANGE = Color.rgb(243, 152, 0);
    private static final int[] COLORS = {Color.RED, Color.YELLOW,
            Color.MAGENTA, Color.GREEN,
            Color.BLUE, ORANGE,
            Color.CYAN};
    private int blockColor = COLORS[mRand.nextInt(COLORS.length)];
    ;
    private int nextColor;
    private static boolean isStop = false;
    NextBlock nextBlock;
    QueueNext nextList = new QueueNext();
    QueueNext isColorList = new QueueNext();
    //private static int[][] start;
    //TODO


    public Block(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, this);
        mHolder = getHolder();
        mHolder.addCallback(this);
        initGame();
        se = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        seSoundId = se.load(getContext(), R.raw.drum02, 1);
        nextBlock = new NextBlock(context);
    }

    private void drawMatrix(Canvas canvas, int[][] matrix, int offsetx, int offsety, int color) {
        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.getPaint().setColor(color);

        int h = matrix.length;
        int w = matrix[0].length;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (matrix[y][x] != 0) {
                    int px = (x + offsetx) * BLOCK_WIDTH;
                    int py = (y + offsety) * BLOCK_HEIGHT;
                    rect.setBounds(px, py, px + BLOCK_WIDTH, py + BLOCK_HEIGHT);
                    rect.draw(canvas);
                }
            }
        }
    }

    void clearRows() {
        //se = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        //seSoundId = se.load(getContext(), R.raw.drum02, 0);
        // 埋まった行は消す。nullで一旦マーキング
        for (int y = 0; y < mapHeight - 1; y++) {
            boolean full = true;
            for (int x = 1; x < mapWidth - 1; x++) {
                if (blockMap[y][x] == 0) {
                    full = false;
                    se.play(seSoundId, 100, 100, 0, 0, 1.0f);
                    break;
                }
            }

            if (full) {
                blockMap[y] = null;
                //se.play(seSoundId, 100, 100, 0, 0, 1.0f);
            }
        }

        // 新しいmapにnull以外の行を詰めてコピーする
        int[][] newMap = new int[mapHeight][];
        int y2 = mapHeight - 1;
        for (int y = mapHeight - 1; y >= 0; y--) {
            if (blockMap[y] == null) {
                continue;
            } else {
                newMap[y2--] = blockMap[y];
            }
        }

        // 消えた行数分新しい行を追加する
        for (int i = 0; i <= y2; i++) {
            int[] newRow = new int[mapWidth];
            for (int j = 0; j < mapWidth; j++) {
                if (j == 0 || j == mapWidth - 1) {
                    newRow[j] = 1;
                } else {
                    newRow[j] = 0;
                }
            }
            newMap[i] = newRow;
            nextBlock.setScore(NextBlock.mDifficulty);
        }
        blockMap = newMap;
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
            return false;
        }
        for (int y = 0; y < block.length; y++) {
            for (int x = 0; x < block[y].length; x++) {
                if (block[y][x] != 0 && blockMap[y + offsety][x + offsetx] != 0) {
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

    //TODO  ランダムにBLOCK生成
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        nextColor = COLORS[mRand.nextInt(COLORS.length)];
        next = blocks[mRand.nextInt(blocks.length)];
        Log.v("next", String.valueOf(next));
/*
        for (int i = 0; i < 2; i++){
            next = blocks[mRand.nextInt(blocks.length)];
            nextColor = COLORS[mRand.nextInt(COLORS.length)];
            Log.v("size", String.valueOf(nextList.size()));
        }
*/
        nextBlock.setNext(next, nextColor);
        NextBlock.loopFlagReversal();


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

    //TODO GAMEスレッド

    @Override
    public void run() {
            while (mIsAttached) {
                if (!isStop) {
                    mCanvas = getHolder().lockCanvas();
                    mCanvas.drawColor(Color.BLACK);

                    drawMatrix(mCanvas, block, posx, posy, blockColor);
                    drawMatrix(mCanvas, blockMap, 0, 0,Color.GRAY);

                    if (frame % fallVelocity == 0) {
                        dropBlock();
                    }

                    frame++;
                    getHolder().unlockCanvasAndPost(mCanvas);
                    if (gameOver()) {
                        Intent intent = new Intent(getContext(), ResultActivity.class);
                        intent.putExtra("scoreResult", NextBlock.score);
                        getContext().startActivity(intent);
                    }
                }
            }

    }

    //TODO BLOCK落下

    public void dropBlock() {
        if (check(block, posx, posy + 1)) {
            posy++;
        } else {
            mergeMatrix(block, posx, posy);
            clearRows();
            posx = (mapWidth / 2) - 1;
            posy = 0;
            block = next;
            blockColor = nextColor;
            next = blocks[mRand.nextInt(blocks.length)];
            nextColor = COLORS[mRand.nextInt(COLORS.length)];

            nextBlock.setNext(next, nextColor);
            NextBlock.loopFlagReversal();

        }
        Log.v("Log", "dropBlock()");
    }






    public boolean gameOver() {
        if (posy == 0 && !check(block, posx, posy)) {
            return true;
        }
        return false;
    }

    void mergeMatrix(int[][] block, int offsetx, int offsety) {
        for (int y = 0; y < block.length; y++) {
            for (int x = 0; x < block[0].length; x++) {
                if (block[y][x] != 0) {
                    blockMap[offsety + y][offsetx + x] = block[y][x];
                }
            }
        }
    }

    public static void loopFlagReversal() {
        isStop = !isStop;
    }

    //TODO

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

    public static void setFallVelocity(String difficulty) {
        if (difficulty.equals("EASY")) {
            fallVelocity = 80;

        } else if (difficulty.equals("NORMAL")) {
            fallVelocity = 50;

        } else if (difficulty.equals("HARD")) {
            fallVelocity = 30;

        }
    }

    public void initGame() {
        for (int y = 0; y < mapHeight; y++) {
            blockMap[y] = new int[mapWidth];
            for (int x = 0; x < mapWidth; x++) {
                //マップの外枠に色を塗る
                if (y == mapHeight - 1 || x == mapWidth - 1
                        || x == 0) {
                    blockMap[y][x] = 1;
                } else {
                    blockMap[y][x] = 0;
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