package com.example.projet_android.commandes;

import com.example.projet_android.MainImage;

public class Dessiner implements ICommande{

    private MainImage.Memento img;
    @Override
    public void executer(MainImage img) {
        this.img = img.sauvegarder();
    }

    @Override
    public void annuler(MainImage img) {
        img.restaurer(this.img);
    }

    @Override
    public void retablir(MainImage img) {
        this.executer(img);
    }

}
