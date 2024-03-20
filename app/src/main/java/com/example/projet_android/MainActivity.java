package com.example.projet_android;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_android.commandes.ImagePicker;

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

        ImagePicker imagePicker = new ImagePicker(this, mainImage);
        ImageButton openFilesBtn = findViewById(R.id.file);
        openFilesBtn.setOnClickListener((view) -> mainController.clickOpenFiles(imagePicker));

        ImageButton undoBtn = findViewById(R.id.undo);
        undoBtn.setOnClickListener((view) -> mainController.clickUndo(mainImage));

        ImageButton redoBtn = findViewById(R.id.redo);
        redoBtn.setOnClickListener((view) -> mainController.clickRedo(mainImage));

        ImageButton saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener((view) -> mainController.clickSave(mainImage));

        View pencilMenu = findViewById(R.id.pen_menu);
        ImageButton handBtn = findViewById(R.id.hand);
        handBtn.setOnClickListener((view) -> mainController.clickHand(mainImage, handBtn, pencilMenu));

        ImageButton pencilBtn = findViewById(R.id.pen);
        pencilBtn.setOnClickListener((view) -> mainController.clickPencil(mainImage, pencilMenu));

        ImageButton rotateBtn = findViewById(R.id.rotate);
        rotateBtn.setOnClickListener((view) -> mainController.clickRotation(mainImage));

        ImageButton eraseBtn = findViewById(R.id.eraser);
        eraseBtn.setOnClickListener(view -> mainController.clickEraser(mainImage));
    }

    private int getColorFromSeekBar(int progress) {
        float[] hsv = new float[]{progress * 360f / 100f, 1f, 1f};
        return Color.HSVToColor(hsv);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("mainImage", ((BitmapDrawable) this.mainImage.getDrawable()).getBitmap());
        float rota = this.mainImage.getRotation();
        savedInstanceState.putFloat("rotation", rota);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bitmap img = savedInstanceState.getParcelable("mainImage");
        float rota = savedInstanceState.getFloat("rotation");
        this.mainImage.setImageBitmap(img);
        this.mainImage.changeRotation('+', (int) rota);
    }
}