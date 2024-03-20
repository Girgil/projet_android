package com.example.projet_android;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageButton;

import com.example.projet_android.commandes.CommandManager;
import com.example.projet_android.commandes.ImagePicker;
import com.example.projet_android.commandes.Rotate;
import com.example.projet_android.commandes.SaveImage;

public class MainController {

    public void clickOpenFiles(ImagePicker imagePicker) {
        imagePicker.pickImage();
    }

    public void clickUndo(MainImage img) {
        CommandManager.getInstance().undoLastCommand(img);
    }

    public void clickRedo(MainImage img) {
        CommandManager.getInstance().redoCommand(img);
    }

    public void clickSave(MainImage img) {
        img.save();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void clickHand(MainImage img, ImageButton view, View penMenu) {
        view.setSelected(!view.isSelected());
        if (penMenu.getVisibility() == View.VISIBLE) {
            penMenu.setVisibility(View.INVISIBLE);
            img.setDrawingMode(false);
        }
        HandController handController = new HandController(img);
        img.setOnTouchListener((v, event) -> handController.onTouchEvent(event));
    }

    @SuppressLint("ClickableViewAccessibility")
    public void clickPencil(MainImage img, View penMenu) {
        int visible = penMenu.getVisibility();
        img.setOnTouchListener(null);
        if(visible == View.VISIBLE) {
            penMenu.setVisibility(View.INVISIBLE);
            img.setDrawingMode(false);
        } else {
            penMenu.setVisibility(View.VISIBLE);
            img.setDrawingMode(true);
        }
    }

    public void clickEraser(MainImage img) {
        SaveImage saveImage = new SaveImage(img);
        img.changeImgToDefault();
        saveImage.execute(img);
        CommandManager.getInstance().addCommand(saveImage);
    }

    public void clickRotation(MainImage img) {
        Rotate rotate = new Rotate();
        rotate.execute(img);
        CommandManager.getInstance().addCommand(rotate);
    }

}
