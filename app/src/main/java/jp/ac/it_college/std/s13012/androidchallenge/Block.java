package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
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

    private int[][] block = blocks[mRand.nextInt(blocks.length)];
    private int mapWidth = 12;
    private int mapHeight = 21;
    private int posx = mapWidth / 2, posy;
    private int[][] blockMap = new int[mapHeight][];
    private final static int BLOCK_SIZE = 53;
    private GestureDetector gestureDetector;
    protected static Thread mThread;
    private SurfaceHolder mHolder;
    public static boolean mIsAttached;
    private Canvas mCanvas = null;
    public static int fallVelocity;
    public static int frame;
    private final int[] COLORS = {Color.RED, Color.YELLOW,
            Color.MAGENTA, Color.GREEN,
            Color.BLUE, Color.argb(255,243,152,0),
            Color.CYAN};
    private int blockColor;



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

    void clearRows() {
        // 埋まった行は消す。nullで一旦マーキング
        for (int y = 0; y < mapHeight - 1; y++) {
            boolean full = true;
            for (int x = 1; x < mapWidth - 1; x++) {
                if (blockMap[y][x] == 0) {
                    full = false;
                    break;
                }
            }

            if (full) {
                blockMap[y] = null;
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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        blockColor = COLORS[mRand.nextInt(COLORS.length)];
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

            drawMatrix(block, posx, posy, blockColor);
            drawMatrix(blockMap, 0, 0, Color.GRAY);

            if (frame % fallVelocity == 0) {
                dropBlock();
            }

            frame++;
            getHolder().unlockCanvasAndPost(mCanvas);
            if (gameOver()){
                Intent intent = new Intent(getContext(),ResultActivity.class);
                getContext().startActivity(intent);
            }

        }
    }

    public void dropBlock() {
        if (check(block, posx, posy + 1)) {
            posy++;
        } else {
            mergeMatrix(block, posx, posy);
            clearRows();
            posx = mapWidth / 2;
            posy = 0;
            block = blocks[mRand.nextInt(blocks.length)];
            blockColor = COLORS[mRand.nextInt(COLORS.length)];
        }
    }

    public boolean gameOver(){
        if(posy == 0 && !check(block, posx, posy)){
            return true;
        }
        return false;
    }

    void mergeMatrix(int[][] block, int offsetx, int offsety) {
        for (int y = 0; y < block.length; y ++) {
            for (int x = 0; x < block[0].length; x ++) {
                if (block[y][x] != 0) {
                    blockMap[offsety + y][offsetx + x] = block[y][x];
                }
            }
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
            blockMap[y] = new int[mapWidth];
            for (int x = 0; x < mapWidth; x++) {
                //マップの外枠に色を塗る
                if (y == mapHeight - 1 || x == mapWidth - 1
                        || x == 0) {
                    blockMap[y][x] = 1;
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
