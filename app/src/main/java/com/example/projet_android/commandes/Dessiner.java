package com.example.projet_android.commandes;

import android.view.View;

public class Dessiner implements ICommande{

    private View view;
    public Dessiner(View view) {
        this.view = view;
    }

    @Override
    public void executer() {
        float rota = this.view.getRotation();
        this.view.setRotation(rota+90);
    }

    @Override
    public void annuler() {
        float rota = this.view.getRotation();
        this.view.setRotation(rota-90);
    }

    @Override
    public void retablir() {
        this.executer();
    }
}
