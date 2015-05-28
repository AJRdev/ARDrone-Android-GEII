package com.example.projet_er4_v1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View {

   

        private final String TAG = "JoystickView";
        private Paint circlePaint;
        private Paint handlePaint;
        private double touchX, touchY;
        private int innerPadding;
        private int handleRadius;
        private int handleInnerBoundaries;
        private JoystickMovedListener listener;
        private int sensitivity;
        
        public double dX ;
        public double dY;

     

        public JoystickView(Context context) {
                super(context);
                initJoystickView();
        }

        public JoystickView(Context context, AttributeSet attrs) {
                super(context, attrs);
                initJoystickView();
        }

        public JoystickView(Context context, AttributeSet attrs, int defStyle) {
                super(context, attrs, defStyle);
                initJoystickView();
        }


        private void initJoystickView() {
                setFocusable(true);
                circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                circlePaint.setColor(Color.rgb(38,196,236));
                circlePaint.setStrokeWidth(1);
                circlePaint.setStyle(Paint.Style.STROKE);

                handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

                handlePaint.setColor(Color.rgb(38,196,236));
                handlePaint.setStrokeWidth(1);
                handlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

                innerPadding = 10;
                sensitivity = 10;
        }



        public void setOnJostickMovedListener(JoystickMovedListener listener) {
                this.listener = listener;
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                // Here we make sure that we have a perfect circle
                int measuredWidth = measure(widthMeasureSpec);
                int measuredHeight = measure(heightMeasureSpec);
                int d = Math.min(measuredWidth, measuredHeight);

                handleRadius = (int)(d * 0.25);
                handleInnerBoundaries = handleRadius;
                
                setMeasuredDimension(d, d);
        }

        private int measure(int measureSpec) {
                int result = 0;
                // Decode the measurement specifications.
                int specMode = MeasureSpec.getMode(measureSpec);
                int specSize = MeasureSpec.getSize(measureSpec);
                if (specMode == MeasureSpec.UNSPECIFIED) {
                        // Return a default size of 200 if no bounds are specified.
                        result = 200;
                } else {
                        // As you want to fill the available space
                        // always return the full available bounds.
                        result = specSize;
                }
                return result;
        }

        @Override
        protected void onDraw(Canvas canvas) {
                int px = getMeasuredWidth() / 2;
                int py = getMeasuredHeight() / 2;
                int radius = Math.min(px, py);

                // Draw the background
                canvas.drawCircle(px, py, radius - innerPadding, circlePaint);

                // Draw the handle
                canvas.drawCircle((int) touchX + px, (int) touchY + py, handleRadius,
                                handlePaint);

                canvas.save();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                int actionType = event.getAction();
                if (actionType == MotionEvent.ACTION_MOVE) {
                        int px = getMeasuredWidth() / 2;
                        int py = getMeasuredHeight() / 2;
                        int radius = Math.min(px, py) - handleInnerBoundaries;

                        touchX = (event.getX() - px);
                        touchX = Math.max(Math.min(touchX, radius), -radius);

                        touchY = (event.getY() - py);
                        touchY = Math.max(Math.min(touchY, radius), -radius);

                        // Coordinates
                        Log.d(TAG, "X:" + touchX + "|Y:" + touchY);
                      
                        // Pressure
                        dX = (double)(touchX / radius * sensitivity);
                        dY = (double) (touchY  / radius * sensitivity);
                        if (listener != null) {
                                listener.OnMoved( (double)(touchX / radius * sensitivity),(double) (touchY  / radius * sensitivity));
                        }

                        invalidate();
                } else if (actionType == MotionEvent.ACTION_UP) {
                        returnHandleToCenter();
                        dX = 0;
                        dY = 0;
                        Log.d(TAG, "X:" + touchX + "|Y:" + touchY);
                }
                return true;
        }

        private void returnHandleToCenter() {

                Handler handler = new Handler();
                int numberOfFrames = 5;
                final double intervalsX = (0 - touchX) / numberOfFrames;
                final double intervalsY = (0 - touchY) / numberOfFrames;

                for (int i = 0; i < numberOfFrames; i++) {
                        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        touchX += intervalsX;
                                        touchY += intervalsY;
                                        invalidate();
                                }
                        }, i * 40);
                }

                if (listener != null) {
                        listener.OnReleased();
                }
        }
}