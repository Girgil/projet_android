package com.example.projet_android.commandes;

import com.example.projet_android.MainImage;

public class Rotate implements ICommand {

    @Override
    public void execute(MainImage img) {
        img.changeRotation('+', 90);
    }

    @Override
    public void undo(MainImage img) {
        img.changeRotation('-', 90);
    }

    @Override
    public void redo(MainImage img) {
        this.execute(img);
    }
}
