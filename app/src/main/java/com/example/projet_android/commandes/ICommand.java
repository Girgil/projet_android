package com.example.projet_android.commandes;

import com.example.projet_android.MainImage;

public interface ICommand {


    void execute(MainImage img);

    void undo(MainImage img);

    void redo(MainImage img);
}
