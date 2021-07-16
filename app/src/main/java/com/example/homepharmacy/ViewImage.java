package com.example.homepharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.firebase.storage.FirebaseStorage;

public class ViewImage extends AppCompatActivity {

    FirebaseStorage mFirebaseStorage;
    ImageView imageView;
    SubsamplingScaleImageView subImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = findViewById(R.id.imgShow);
        Intent intent = getIntent();
        String url = intent.getStringExtra("image");
        //StorageReference mImage = mFirebaseStorage.getReferenceFromUrl(url);
        Glide.with(ViewImage.this).load(url).into(imageView);

    }
}