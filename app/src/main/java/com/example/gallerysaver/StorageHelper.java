package com.example.gallerysaver;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageHelper {

    private Context applicationContext;
    private Uri collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private String[] projection = {MediaStore.Images.Media.DATA, };

    public StorageHelper(@NonNull Context context) {
        this.applicationContext = context;
        loadStorageImages();
    }

    private Map<String, ArrayList<Image>> galleryFolders = new HashMap<>();

    void loadStorageImages() {
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        try (Cursor cursor = applicationContext.getContentResolver().query(
                collection,
                projection,
                null,
                null,
                sortOrder
        )) {
            assert cursor != null;
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext()) {
                String imgUri = cursor.getString(idColumn);
                String folderName = new File(imgUri).getParentFile().getName();
                if(galleryFolders.containsKey(folderName)) {
                    galleryFolders.get(folderName).add(new Image(imgUri));
                } else {
                    ArrayList<Image> images = new ArrayList<>();
                    images.add(new Image(imgUri));
                    galleryFolders.put(folderName, images);
                }
            }
        }
    }

    public Map<String, ArrayList<Image>> getGalleryFolders() {
        return galleryFolders;
    }

}
