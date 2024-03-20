package com.example.projet_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_android.commandes.GestionnaireCommande;
import com.example.projet_android.commandes.ICommande;
import com.example.projet_android.commandes.Pivoter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton penButton;
    private SeekBar colorSeekBar;
    private RadioGroup radioGroup;
    private View colorPreview;
    private int penColor = Color.RED;
    private int drawWidth = 10;
    private MainImage mainImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        penButton = findViewById(R.id.pen);
        colorSeekBar = findViewById(R.id.colorSeekBar);
        radioGroup = findViewById(R.id.pen_size_group);
        mainImage = findViewById(R.id.imageView);

        colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Mettez à jour la couleur en fonction de la position du curseur
                int color = getColorFromSeekBar(progress);
                colorSeekBar.getThumb().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)); // Mettez à jour la couleur du bouton pen
                mainImage.setPenColor(color);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int small = group.findViewById(R.id.pen_size_small).getId();
                int medium = group.findViewById(R.id.pen_size_medium).getId();
                int large = group.findViewById(R.id.pen_size_large).getId();
                Log.d("Mes Logs", "Check: "+checkedId);
                if(checkedId == small)
                    mainImage.setPenWidth(5);
                if(checkedId == medium)
                    mainImage.setPenWidth(10);
                if(checkedId == large)
                    mainImage.setPenWidth(15);
            }
        });
    }

    private int getColorFromSeekBar(int progress) {
        float[] hsv = new float[]{progress * 360f / 100f, 1f, 1f};
        return Color.HSVToColor(hsv);
    }

    // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    MainImage imgView = findViewById(R.id.imageView);
                    imgView.setRotation(0);
                    imgView.setImageURI(uri);
                    Log.d("Mes logs", uri.toString());
                } else {
                    Log.d("Mes logs", "No media selected");
                }
            });

    public void imageChooser(View view) {

        view.setPressed(true);
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    public void pivoterControleur(View view) {
        view.setPressed(true);
        Pivoter pivoter = new Pivoter();
        pivoter.executer(mainImage);
        GestionnaireCommande.getInstance().addCommande(pivoter);
    }

    public void annulerControleur(View view) {
//        item.setChecked(true);
        GestionnaireCommande.getInstance().undoLastCommand(mainImage);
    }

    public void retablirControleur(View view) {
//        item.setChecked(true);
        view.setPressed(true);
        GestionnaireCommande.getInstance().redoCommand(mainImage);
    }

    public void enregistrer(View view) {
        mainImage.save();
    }

    public void showPenMenu(View view){
        View vue = findViewById(R.id.pen_menu);
        int visible = vue.getVisibility();
        if(visible == View.VISIBLE) {
            vue.setVisibility(View.INVISIBLE);
        }else {
            vue.setVisibility(View.VISIBLE);
        }
        mainImage.switchDrawingMode();
    }

    private void deactivateDrawingMode() {
        // Supprimer les écouteurs d'événements de dessin
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnTouchListener(null);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state.
        savedInstanceState.putParcelable("mainImage", ((BitmapDrawable) this.mainImage.getDrawable()).getBitmap());
        float rota = this.mainImage.getRotation();
        savedInstanceState.putFloat("rotation", rota);
        // Always call the superclass so it can save the view hierarchy state.
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy.
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance.
        Bitmap img = savedInstanceState.getParcelable("mainImage");
        float rota = savedInstanceState.getFloat("rotation");
        this.mainImage.setImageBitmap(img);
        this.mainImage.changeRotation('+', (int) rota);
    }

}