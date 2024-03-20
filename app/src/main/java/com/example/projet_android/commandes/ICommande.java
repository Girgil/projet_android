package com.example.projet_android.commandes;

import com.example.projet_android.MainImage;

public interface ICommande {


    void executer(MainImage img);

    void annuler(MainImage img);

    void retablir(MainImage img);
}
