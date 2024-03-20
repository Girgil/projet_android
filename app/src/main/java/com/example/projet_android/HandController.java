package com.example.projet_android;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class HandController extends Activity {

    private ScaleGestureDetector scaleGestureDetector;
    private ImageView imageView;

    public HandController(ImageView imageView) {
        this.imageView = imageView;
        scaleGestureDetector = new ScaleGestureDetector(imageView.getContext(), new ScaleListener());
    }

    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float currentScale = imageView.getScaleX();
            float newScale = currentScale * scaleFactor;

            // Minimum and maximum scale
            float minScale = 0.1f;
            float maxScale = 5.0f;
            newScale = Math.max(minScale, Math.min(newScale, maxScale));

            imageView.setScaleX(newScale);
            imageView.setScaleY(newScale);

            return true;
        }
    }
}
