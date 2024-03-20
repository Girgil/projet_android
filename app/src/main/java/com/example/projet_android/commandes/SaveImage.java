package com.example.projet_android.commandes;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.projet_android.MainImage;

public class SaveImage implements ICommand {

    private final Bitmap oldImage;

    private Bitmap newImage;
    
    public SaveImage(MainImage oldImage) {
        this.oldImage = ((BitmapDrawable) oldImage.getDrawable()).getBitmap();
    }
    @Override
    public void execute(MainImage img) {
        this.newImage = ((BitmapDrawable) img.getDrawable()).getBitmap();
    }

    @Override
    public void undo(MainImage img) {
        img.setImageBitmap(oldImage);

    }

    @Override
    public void redo(MainImage img) {
        img.setImageBitmap(newImage);
    }

}
