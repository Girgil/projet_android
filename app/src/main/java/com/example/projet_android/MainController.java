package com.example.projet_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.projet_android.commandes.GestionnaireCommande;
import com.example.projet_android.commandes.Pivoter;

public class MainController {

    public void clickOpenFiles(ImageButton view, ActivityResultLauncher<PickVisualMediaRequest> pickMedia) {
        view.setActivated(true);
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    public void clickUndo(MainImage img) {
        GestionnaireCommande.getInstance().undoLastCommand(img);
    }

    public void clickRedo(MainImage img) {
        GestionnaireCommande.getInstance().redoCommand(img);
    }

    public void clickSave(MainImage img) {
        img.save();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void clickHand(MainImage img, ImageButton view, View penMenu) {
        view.setSelected(!view.isSelected());
        view.setColorFilter(Color.GREEN);
        if (penMenu.getVisibility() == View.VISIBLE) {
            penMenu.setVisibility(View.INVISIBLE);
            img.setDrawingMode(false);
        }
        HandController handController = new HandController(img);
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return handController.onTouchEvent(event);
            }
        });
    }

    public void clickPencil(MainImage img, ImageButton view, View penMenu) {
        view.setPressed(true);
        int visible = penMenu.getVisibility();
        img.setOnTouchListener(null);
        if(visible == View.VISIBLE) {
            penMenu.setVisibility(View.INVISIBLE);
            img.setDrawingMode(false);
        }else {
            penMenu.setVisibility(View.VISIBLE);
            img.setDrawingMode(true);
        }
    }

    public void clickRotation(MainImage img, ImageButton view) {
        view.setActivated(true);
        Pivoter pivoter = new Pivoter();
        pivoter.executer(img);
        GestionnaireCommande.getInstance().addCommande(pivoter);
    }

}
