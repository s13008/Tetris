package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class NextBlock extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    public static int mDifficulty;
    private TextView scoreText;
    private TextView nextText;
    public static int score = 0;
    private static final int EASY = 0;
    private static final int NORMAL = 1;
    private static final int HARD = 2;
    public static final int EASY_SCORE = 100;
    public static final int NORMAL_SCORE = 200;
    public static final int HARD_SCORE = 300;
    private static int[][] nextBlock;
    private static int nextBlockColor;
    protected static Thread mThread;
    private SurfaceHolder mHolder;
    public static boolean mIsAttached;
    private Canvas mCanvas = null;
    private static boolean isStop = true;


    public NextBlock(Context context) {
        super(context);
        scoreText = (TextView) ((TetrisActivity) context).findViewById(R.id.score);
        mHandler.sendEmptyMessage(0);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public static void setDifficulty(String difficulty) {
        if (difficulty.equals("EASY")) {
            mDifficulty = EASY;

        } else if (difficulty.equals("NORMAL")) {
            mDifficulty = NORMAL;

        } else if (difficulty.equals("HARD")) {
            mDifficulty = HARD;
        }
    }

    public void setScore(int difficulty) {
        switch (difficulty) {
            case EASY:
                mHandler.sendEmptyMessage(EASY_SCORE);
                break;
            case NORMAL:
                mHandler.sendEmptyMessage(NORMAL_SCORE);
                break;
            case HARD:
                mHandler.sendEmptyMessage(HARD_SCORE);
                break;
        }
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            score += msg.what;
            scoreText.setText("Score: " + String.valueOf(score));
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
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
            if (!isStop) {
                mCanvas = getHolder().lockCanvas();
                mCanvas.drawColor(Color.BLACK);
                drawMatrix(mCanvas, nextBlock, nextBlockColor);
                getHolder().unlockCanvasAndPost(mCanvas);
                isStop = !isStop;
            }
        }
    }

    public static void loopFlagReversal() {
        isStop = !isStop;
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

    public void setNext(int[][] nextBlock, int nextBlockColor) {
        this.nextBlock = nextBlock;
        this.nextBlockColor = nextBlockColor;
    }

    private void drawMatrix(Canvas canvas, int[][] matrix, int color) {
        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.getPaint().setColor(color);

        int h = matrix.length;
        int w = matrix[0].length;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (matrix[y][x] != 0) {
                    int px = x * Block.BLOCK_WIDTH;
                    int py = y * Block.BLOCK_HEIGHT;
                    rect.setBounds(px, py, px + Block.BLOCK_WIDTH, py + Block.BLOCK_HEIGHT);
                    rect.draw(canvas);
                }
            }
        }
    }
}
