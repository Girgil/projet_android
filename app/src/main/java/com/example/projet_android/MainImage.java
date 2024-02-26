package com.example.projet_android;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class MainImage extends AppCompatImageView {
    public MainImage(Context context) {
        super(context);
    }

    public MainImage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainImage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
