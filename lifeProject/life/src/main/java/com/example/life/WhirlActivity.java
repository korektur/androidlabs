package com.example.life;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;


public class WhirlActivity extends Activity {


    class WhirlView extends View {
        Paint p;
        int WIDTH = 240;
        int HEIGHT = 320;
        int[][] field;
        int[][] new_step_field;
        int[][] colors;
        Recounter rec;
        Thread thread;

        public WhirlView(Context context) {
            super(context);
            p = new Paint();
            field = new int[WIDTH][HEIGHT];
            new_step_field = new int[WIDTH][HEIGHT];
            colors = new int[16][4];
            Random rand = new Random();
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 4; j++) {
                    colors[i][j] = rand.nextInt(256);
                }
            }
            for (int i = 0; i < WIDTH; i++)
                for (int j = 0; j < HEIGHT; j++) {
                    field[i][j] = rand.nextInt(16);
                }
            rec = new Recounter();
            thread = new Thread(rec);
            thread.start();
        }

        @Override
        public void onDraw(Canvas canvas) {

            for (int i = 0; i < WIDTH; ++i) {
                for (int j = 0; j < HEIGHT; ++j) {
                    field[i][j] = new_step_field[i][j];
                }
            }
            rec.notifyAll();
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    p.setARGB(colors[field[i][j]][0], colors[field[i][j]][1],
                            colors[field[i][j]][2], colors[field[i][j]][3]);
                    canvas.drawPoint(i, j, p);
                    canvas.drawRect(3 * i, 4 * j, 3 * (i + 1), 4 * (j + 1), p);
                }
            }

            invalidate();
        }

        public class Recounter implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < WIDTH; ++i)
                    for (int j = 0; j < HEIGHT; ++j) {
                        new_step_field[i][j] = field[i][j];
                    }
                for (int i = 0; i < WIDTH; i++) {
                    for (int j = 0; j < HEIGHT; j++) {
                        if (i < WIDTH - 1 && (field[i + 1][j] + 1) % 16 == field[i][j]) {
                            new_step_field[i][j] = field[i + 1][j];
                        } else if (j < HEIGHT - 1 && (field[i][j + 1] + 1) % 16 == field[i][j]) {
                            new_step_field[i][j] = field[i][j + 1];
                        } else if (i < WIDTH - 1 && j < HEIGHT - 1 && (field[i + 1][j + 1] + 1) % 16 == field[i][j]) {
                            new_step_field[i][j] = field[i + 1][j + 1];
                        } else if (i > 0 && (field[i - 1][j] + 1) % 16 == field[i][j]) {
                            new_step_field[i][j] = field[i - 1][j];
                        } else if (j > 0 && (field[i][j - 1] + 1) % 16 == field[i][j]) {
                            new_step_field[i][j] = field[i][j - 1];
                        } else if (i > 0 && j > 0 && (field[i - 1][j - 1] + 1) % 16 == field[i][j]) {
                            new_step_field[i][j] = field[i - 1][j - 1];
                        } else if (i > 0 && j < HEIGHT - 1 && (field[i - 1][j + 1] + 1) % 16 == field[i][j]) {
                            new_step_field[i][j] = field[i - 1][j + 1];
                        } else if (i < WIDTH - 1 && j > 0 && (field[i + 1][j - 1] + 1) % 16 == field[i][j]) {
                            new_step_field[i][j] = field[i + 1][j - 1];
                        }
                    }
                }
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                invalidate();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new WhirlView(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.whirl, menu);
        return true;
    }

}
