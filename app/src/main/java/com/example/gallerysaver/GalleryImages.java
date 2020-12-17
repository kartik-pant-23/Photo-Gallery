package com.example.gallerysaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GalleryImages extends AppCompatActivity {

    private ArrayList<Image> selectedImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        ArrayList<Image> images = intent.getParcelableArrayListExtra("images");
        String folderName = intent.getStringExtra("folder_name");

        RecyclerView galleryRcv = findViewById(R.id.galleryRCV);
        galleryRcv.setHasFixedSize(true);
        galleryRcv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));

        GalleryAdapter galleryAdapter = new GalleryAdapter(getApplicationContext(), images);
        galleryRcv.setAdapter(galleryAdapter);

        Button okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(view -> {
            /**
             * TODO
             * Handle this callback.
             * Do whatever you wish to do with the selected images.
             * ArrayList selectedImages holds path of selected images.
             */
        });

        TextView headerText = findViewById(R.id.headerText);
        headerText.setText(folderName);

        galleryAdapter.setOnItemClickListener(image -> {
            if(image.isChecked()) {
                selectedImages.add(image);
                String headerTxt = selectedImages.size()+" "+getString(R.string.photo_gallery_selected);
                headerText.setText(headerTxt);
                okBtn.setVisibility(View.VISIBLE);
            } else {
                for(int i=0; i<selectedImages.size(); i++) {
                    if(selectedImages.get(i).getImgUri().equals(image.getImgUri())) {
                        selectedImages.remove(i);
                        break;
                    }
                }
                if(selectedImages.size()==0) {
                    headerText.setText(folderName);
                    okBtn.setVisibility(View.GONE);
                } else {
                    String headerTxt = selectedImages.size()+" "+getString(R.string.photo_gallery_selected);
                    headerText.setText(headerTxt);
                }
            }
        });

    }

}