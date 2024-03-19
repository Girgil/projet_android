package com.example.projet_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class MainImage extends AppCompatImageView {
    private Path drawingPath;
    private Paint drawPaint;
    private int penColor = Color.RED;
    private int penWidth = 10;
    private boolean drawingEnabled = false;

    public MainImage(Context context) {
        super(context);
    }

    public MainImage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public MainImage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setupDrawing() {
        drawingPath = new Path();
        drawPaint = new Paint();
        drawPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(drawingEnabled)
            canvas.drawPath(drawingPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        if (!drawingEnabled) return false;
        float x = event.getX();
        float y = event.getY();
        drawPaint.setColor(getPenColor());
        drawPaint.setStrokeWidth(getPenWidth());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Début du tracé, déplacer le Path vers le point de contact
                drawingPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                // Ajouter le point de contact au Path
                drawingPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                // Rien à faire à la fin du tracé
                drawingPath.reset();
                break;
        }

        // Forcer la répétition de la méthode onDraw pour mettre à jour l'affichage
        //invalidate();
        this.setImageBitmap(bitmap);

        return true; // Indiquer que l'événement de toucher est consommé
    }

    public void switchDrawingMode(){
        if(this.drawingEnabled)
            this.drawingEnabled = false;
        else
            this.drawingEnabled = true;
    }

    public int getPenColor() {
        return this.penColor;
    }

    public void setPenColor(int penColor){
        this.penColor = penColor;
    }

    public int getPenWidth(){
        return this.penWidth;
    }

    public void setPenWidth(int penWidth){
        this.penWidth = penWidth;
    }
}

