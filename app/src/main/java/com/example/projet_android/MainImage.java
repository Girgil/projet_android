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

import com.example.projet_android.commandes.CommandManager;
import com.example.projet_android.commandes.SaveImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainImage extends AppCompatImageView {

    private static Bitmap DEFAULT_BITMAP;

    private Path drawingPath;
    private Paint drawPaint;
    private int penColor = Color.RED;
    private int penWidth = 10;
    private boolean drawingEnabled = false;
    private SaveImage currentDraw;

    private final Matrix matrix = new Matrix();

    public MainImage(Context context) {
        super(context);
    }

    public MainImage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
        DEFAULT_BITMAP = ((BitmapDrawable) this.getDrawable()).getBitmap();
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

    public void resetRotation() {
        this.setRotation(0);
        matrix.setRotate(0);
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

    public void setDefaultBitmap() {
        this.setImageBitmap(DEFAULT_BITMAP);
    }

    public void changeDefaultBitmap(Bitmap bitmap) {
        DEFAULT_BITMAP = bitmap;
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
        if (!drawingEnabled) return false;
        this.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        float x = event.getX();
        float y = event.getY();
        drawPaint.setColor(this.penColor);
        drawPaint.setStrokeWidth(this.penWidth);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // On appuie sur l'image : on ne dessine pas, on bouge juste le curseur
                drawingPath.moveTo(x, y);
                // On réinitialise le dessin
                this.currentDraw = null;
                break;
            case MotionEvent.ACTION_MOVE:
                // On commence à dessiner
                if (this.currentDraw == null)
                    this.currentDraw = new SaveImage(this);
                drawingPath.lineTo(x, y);
                break;

            case MotionEvent.ACTION_UP:
                // Quand on lève le doigt de l'écran : fin du dessin
                drawingPath.reset();
                if (this.currentDraw != null) {
                    this.currentDraw.execute(this);
                    CommandManager.getInstance().addCommand(this.currentDraw);
                }
                break;
        }

        // On met à jour le bitmap de l'imageview
        this.setImageBitmap(bitmap);
        return true; // Indiquer que l'événement de toucher est consommé
    }

    public void setDrawingMode(boolean bool){
        this.drawingEnabled = bool;
    }

    public void setPenColor(int penColor){
        this.penColor = penColor;
    }
    public void setPenWidth(int penWidth){
        this.penWidth = penWidth;
    }
}

