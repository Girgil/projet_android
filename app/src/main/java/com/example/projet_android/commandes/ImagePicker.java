package com.example.projet_android.commandes;

import android.graphics.Bitmap;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_android.MainImage;

import java.io.IOException;

public class ImagePicker {
    private final ActivityResultLauncher<String> launcher;

    public ImagePicker(AppCompatActivity activity, MainImage img) {
        launcher = activity.registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), result);
                            SaveImage saveImage = new SaveImage(img);
                            img.setImageBitmap(bitmap);
                            img.resetRotation();
                            saveImage.execute(img);
                            CommandManager.getInstance().addCommand(saveImage);
                            img.changeDefaultBitmap(bitmap);
                        } catch (IOException e) {
                            img.setDefaultBitmap();
                            throw new RuntimeException(e);
                        }
                    } else {
                        img.setDefaultBitmap();
                    }
                });
    }

    public void pickImage() {
        launcher.launch("image/*");
    }
}
