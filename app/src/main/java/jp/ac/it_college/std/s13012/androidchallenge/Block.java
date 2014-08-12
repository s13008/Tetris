package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

import java.util.Random;


public class Block extends View {
    public Block(Context context) {
        super(context);
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
    int posx, posy;
    int mapWidth  = 10;
    int mapHeight = 20;
    int[][] map = new int[mapHeight][];

    @Override
    protected void onDraw(Canvas canvas) {
        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.getPaint().setColor(Color.RED);
        int h = block.length;
        int w = block[0].length;

        for (int y = 0; y < h; y ++) {
            for (int x = 0; x < w; x ++) {
                if (block[y][x] != 0) {
                    int px = (x + posx) * 20;
                    int py = (y + posy) * 20;
                    rect.setBounds(0, 0, px + 20, py + 20);
                    rect.draw(canvas);
                }
            }
        }
    }
}
