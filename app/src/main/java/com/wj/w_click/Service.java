package com.wj.w_click;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by jiwang on 2019/10/17.
 */

public class Service extends WallpaperService {
    private Timer mTimer = null;
    private Click click ;
    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    class MyEngine extends Engine{

        private Handler handler;

        private Paint mPain;
        private Timer timer;
        /**
         * 准备画笔
         */
        public MyEngine() {
            mPain=new Paint();
            mPain.setColor(Color.RED);
            mPain.setAntiAlias(true);
            mPain.setTextSize(60f);
            mPain.setTextAlign(Paint.Align.CENTER);

            click=new Click(getApplicationContext());

            handler=new Handler();

        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            Log.e("clock", "onSurfaceCreated");
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.e("clock", "onSurfaceChanged");
        }

        public void onVisibilityChanged(boolean visible){
            Log.e("clock", "onVisibilityChanged >>"+visible);
            if (visible){
                timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                click.doInvalidate();
                            }
                        });

                    }
                };
                timer.schedule(task, 1000, 1000);
            }

        }

        /**
         * 停止绘制
         */
        private void stopClock() {
            mTimer.cancel();
            mTimer = null;
            click.stopInvalidate();
        }


    }
}
