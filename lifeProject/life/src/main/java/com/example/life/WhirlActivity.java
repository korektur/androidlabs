package com.example.life;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.graphics.Color;
import java.util.Random;
import android.view.WindowManager;

public class WhirlActivity extends Activity {


    class WhirlView extends SurfaceView implements Runnable {
        Paint p;
        int WIDTH = 240;
        int HEIGHT = 320;
        int num_of_colors = 16;
        int[][] field;
        int[][] colors;
        Thread thread = null;
        volatile boolean thread_is_running = true;
        SurfaceHolder surfaceHolder;
        long start_time, end_time;
        int[][] new_step_field;

        public WhirlView(Context context) {
            super(context);
            p = new Paint();
            field = new int[WIDTH][HEIGHT];
            Random rand = new Random();
            colors = new int[num_of_colors][4];
            for(int i = 0; i < num_of_colors; ++i){
                for(int j = 0; j < 4; ++j){
                    colors[i][j] = rand.nextInt(256);
                }
            }
            for (int i = 0; i < WIDTH; ++i)
                for (int j = 0; j < HEIGHT; ++j) {
                    field[i][j] = rand.nextInt(num_of_colors);
                }
            surfaceHolder = getHolder();
            thread = new Thread(this);
            start_time = System.currentTimeMillis();
            thread.start();
        }

        @Override
        public void onDraw(Canvas canvas) {
            for (int i = 0; i < WIDTH; ++i) {
                for (int j = 0; j < HEIGHT; ++j) {
                    p.setARGB(colors[field[i][j]][0], colors[field[i][j]][1], colors[field[i][j]][2], colors[field[i][j]][3]);
                    canvas.drawRect(3 * i, 4 * j, 3 * (i + 1), 4 * (j + 1), p);

                }
            }
            p.setColor(Color.BLACK);
            canvas.drawRect(0, 0, 100, 50, p);
            p.setColor(Color.YELLOW);
            p.setTextSize(20);
            end_time = System.currentTimeMillis();
            canvas.drawText("FPS: " + 1000 / (end_time - start_time), 10, 30, p);
            start_time = end_time;
        }



        public void recount(){
            new_step_field = new int[WIDTH][HEIGHT];
            for (int i = 0; i < WIDTH; ++i) {
                for (int j = 0; j < HEIGHT; ++j) {
                    new_step_field[i][j] = field[i][j];
                    if (i < WIDTH - 1 && (field[i][j] + 1) % 16 == field[i + 1][j]) {
                        new_step_field[i][j] = field[i + 1][j];
                    } else if (j < HEIGHT - 1 && (field[i][j] + 1) % 16 == field[i][j + 1]) {
                        new_step_field[i][j] = field[i][j + 1];
                    } else if (i < WIDTH - 1 && j < HEIGHT - 1 && (field[i][j] + 1) % 16 == field[i + 1][j + 1]) {
                        new_step_field[i][j] = field[i + 1][j + 1];
                    } else if (i > 0 && (field[i][j] + 1) % 16 == field[i - 1][j]) {
                        new_step_field[i][j] = field[i - 1][j];
                    } else if (j > 0 && (field[i][j] + 1) % 16 == field[i][j - 1]) {
                        new_step_field[i][j] = field[i][j - 1];
                    } else if (i > 0 && j > 0 && (field[i][j] + 1) % 16 == field[i - 1][j - 1]) {
                        new_step_field[i][j] = field[i - 1][j - 1];
                    } else if (i > 0 && j < HEIGHT - 1 && (field[i][j] + 1) % 16 == field[i - 1][j + 1]) {
                        new_step_field[i][j] = field[i - 1][j + 1];
                    } else if (i < WIDTH - 1 && j > 0 && (field[i][j] + 1) % 16 == field[i + 1][j - 1]) {
                        new_step_field[i][j] = field[i + 1][j - 1];
                    }
                }
            }
            field = new_step_field;
        }

        public void run() {
            while (thread_is_running) {
                if (surfaceHolder.getSurface().isValid()) {
                    Canvas canvas = surfaceHolder.lockCanvas();
                    recount();
                    onDraw(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new WhirlView(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.whirl, menu);
        return true;
    }

}
