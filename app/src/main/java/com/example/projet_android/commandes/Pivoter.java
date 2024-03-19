package com.example.projet_android.commandes;

import com.example.projet_android.MainImage;

public class Pivoter implements ICommande {

    private final MainImage view;

    public Pivoter(MainImage view) {
        this.view = view;
    }

    @Override
    public void executer() {
        this.view.changeRotation('+', 90);
    }

    @Override
    public void annuler() {
        this.view.changeRotation('-', 90);
    }

    @Override
    public void retablir() {
        this.executer();
    }
}
