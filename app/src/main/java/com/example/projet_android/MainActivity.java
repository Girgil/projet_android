package com.example.projet_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.graphics.PorterDuffColorFilter;

import androidx.appcompat.app.AppCompatActivity;


import com.example.projet_android.commandes.GestionnaireCommande;
import com.example.projet_android.commandes.Pivoter;

public class MainActivity extends AppCompatActivity {

    private int SELECT_PICTURE = 200;
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

    public void imageChooser(View view) {

//        item.setChecked(true);

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    MainImage imgView = findViewById(R.id.imageView);
                    imgView.setImageURI(selectedImageUri);
                }
            }
        }
    }

    public void rotate(View view) {
//        item.setChecked(true);
        View img = findViewById(R.id.imageView);
        Pivoter pivoter = new Pivoter(img);
        pivoter.executer();
        GestionnaireCommande.getInstance().addCommande(pivoter);
        Log.d("Mes logs", "Rotation droite");
        Log.d("Mes logs", GestionnaireCommande.getInstance().toString());
    }

    public void annuler(View view) {
//        item.setChecked(true);
        GestionnaireCommande.getInstance().undoLastCommand();
        Log.d("Mes logs", "Annulation");
        Log.d("Mes logs", GestionnaireCommande.getInstance().toString());
    }

    public void retablir(View view) {
//        item.setChecked(true);
        GestionnaireCommande.getInstance().redoCommand();
        Log.d("Mes logs", "Retablissement");
        Log.d("Mes logs", GestionnaireCommande.getInstance().toString());
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


}