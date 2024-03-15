package com.example.projet_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_android.commandes.GestionnaireCommande;
import com.example.projet_android.commandes.Pivoter;

public class MainActivity extends AppCompatActivity {

    private int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}