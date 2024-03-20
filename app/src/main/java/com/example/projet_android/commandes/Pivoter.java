package com.example.projet_android.commandes;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.example.projet_android.MainImage;

public class Pivoter implements ICommande {

    @Override
    public void executer(MainImage img) {
        img.changeRotation('+', 90);
    }

    @Override
    public void annuler(MainImage img) {
        img.changeRotation('-', 90);
    }

    @Override
    public void retablir(MainImage img) {
        this.executer(img);
    }
}
