package com.example.projet_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MainImage mainImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainController mainController = new MainController();

        mainImage = findViewById(R.id.imageView);

        SeekBar colorSeekBar = findViewById(R.id.colorSeekBar);
        colorSeekBar.getThumb().setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP));
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

        RadioGroup radioGroup = findViewById(R.id.pen_size_group);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
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
        });

        ImageButton openFilesBtn = findViewById(R.id.file);
        openFilesBtn.setOnClickListener((view) -> mainController.clickOpenFiles((ImageButton) view, pickMedia));

        ImageButton undoBtn = findViewById(R.id.undo);
        undoBtn.setOnClickListener((view) -> mainController.clickUndo(mainImage));

        ImageButton redoBtn = findViewById(R.id.redo);
        redoBtn.setOnClickListener((view) -> mainController.clickRedo(mainImage));

        ImageButton saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener((view) -> mainController.clickSave(mainImage));

        View pencilMenu = findViewById(R.id.pen_menu);
        ImageButton handBtn = findViewById(R.id.hand);
        handBtn.setOnClickListener((view) -> mainController.clickHand(mainImage, (ImageButton) view, pencilMenu));

        ImageButton pencilBtn = findViewById(R.id.pen);
        pencilBtn.setOnClickListener((view) -> mainController.clickPencil(mainImage, (ImageButton) view, pencilMenu));

        ImageButton rotateBtn = findViewById(R.id.rotate);
        rotateBtn.setOnClickListener((view) -> mainController.clickRotation(mainImage, (ImageButton) view));
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