package com.example.projet_android.commandes;

import com.example.projet_android.MainImage;

public class Dessiner implements ICommande{


    @Override
    public void executer(MainImage img) {

    }

    @Override
    public void annuler(MainImage img) {

    }

    @Override
    public void retablir(MainImage img) {
        this.executer(img);
    }

}
