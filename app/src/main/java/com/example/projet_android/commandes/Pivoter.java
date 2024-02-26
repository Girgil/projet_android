package com.example.projet_android.commandes;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.projet_android.R;

public class Pivoter implements ICommande {


    private View view;
    public Pivoter(View view) {
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
