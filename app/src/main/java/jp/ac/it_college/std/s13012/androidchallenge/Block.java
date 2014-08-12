package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.Random;


public class Block extends View {
    public Block(Context context) {
        super(context);
        //ディスプレイサイズ取得
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        System.out.println("displayWidth: " + point.x);
        System.out.println("displayHeight: " + point.y);
    }

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
    int canvasSize;
    public int posx, posy;
    int[][] map = new int[mapHeight][];
    private final static int BLOCK_SIZE = 30;

    @Override
    protected void onDraw(Canvas canvas) {
        paintMatrix(canvas, block, posx, posy, Color.RED);
    }

    private void paintMatrix(Canvas canvas, int[][] matrix, int offsetx, int offsety, int color) {
        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.getPaint().setColor(color);
        int h = matrix.length;
        int w = matrix[0].length;

        for (int y = 0; y < h; y ++) {
            for (int x = 0; x < w; x ++) {
                if (matrix[y][x] != 0) {
                    int px = (x + offsetx) * BLOCK_SIZE;
                    int py = (y + offsety) * BLOCK_SIZE;
                    rect.setBounds(px, py, px + BLOCK_SIZE, py + BLOCK_SIZE);
                    rect.draw(canvas);
                }
            }
        }
    }


}
