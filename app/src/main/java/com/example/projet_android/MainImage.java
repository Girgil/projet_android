package com.example.projet_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainImage extends AppCompatImageView {
    private Path drawingPath;
    private Paint drawPaint;
    private int penColor = Color.RED;
    private int penWidth = 10;
    private boolean drawingEnabled = false;

    private final Matrix matrix = new Matrix();

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

    public void changeRotation(char signe, int degree) {
        float rota = this.getRotation();
        if (signe == '+') rota += degree; else rota -= degree;
        this.setRotation(rota);
        matrix.setRotate(rota);
    }

    public void save() {
        Bitmap bitmapImage = ((BitmapDrawable) this.getDrawable()).getBitmap();
        Bitmap newBit = Bitmap.createBitmap(bitmapImage, 0, 0, bitmapImage.getWidth(), bitmapImage.getHeight(), this.matrix, true);

        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File file = new File(directory, "image_tournee.jpg");

        try {
            // Enregistrer l'image tournée dans le fichier.
            FileOutputStream out = new FileOutputStream(file);
            newBit.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            // Actualiser la galerie pour afficher la nouvelle image.
            MediaScannerConnection.scanFile(this.getContext(),
                    new String[]{file.toString()}, null,
                    (path, uri) -> {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @SuppressLint("ClickableViewAccessibility")
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
        this.drawingEnabled = !this.drawingEnabled;
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

