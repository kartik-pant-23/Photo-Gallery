package com.example.gallerysaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

public class PhotoGallery extends AppCompatActivity {

    private RecyclerView galleryFoldersRCV;
    private ProgressBar progressBar;

    private final int ALL_PERMISSIONS = 100;
    private final String[] allPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        galleryFoldersRCV = findViewById(R.id.galleryRCV);
        progressBar = findViewById(R.id.progressBar);

        galleryFoldersRCV.setHasFixedSize(true);
        galleryFoldersRCV.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        if(checkAllPermissionsGranted()) {
            loadFolders();
        } else {
            requestPermissions(allPermissions, ALL_PERMISSIONS);
        }
    }

    private void loadFolders() {
        StorageHelper storageHelper = new StorageHelper(getApplicationContext());
        Map<String, ArrayList<Image>> folders = storageHelper.getGalleryFolders();

        FoldersAdapter adapter = new FoldersAdapter(getApplicationContext(), folders);
        galleryFoldersRCV.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    boolean checkAllPermissionsGranted() {
        for(String permission: allPermissions) {
            if(ActivityCompat.checkSelfPermission(this, permission)!=
                    PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==ALL_PERMISSIONS) {
            boolean storage = grantResults[0]==PackageManager.PERMISSION_GRANTED;
            if(!storage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PhotoGallery.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            } else {
                loadFolders();
            }
        }
    }
}

class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.ViewHolder> {

    private Context context;
    private Map<String, ArrayList<Image>> folders;
    private ArrayList<String> folderNames = new ArrayList<>();

    public FoldersAdapter(Context context, Map<String, ArrayList<Image>> folders) {
        this.context = context;
        this.folders = folders;
        folderNames.addAll(folders.keySet());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView folderImage;
        TextView folderName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderImage = itemView.findViewById(R.id.folderImage);
            folderName = itemView.findViewById(R.id.folderName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String placeHolderText = folderNames.get(position)+" ("+folders.get(folderNames.get(position)).size()+")";
        holder.folderName.setText(placeHolderText);
        String img = folders.get(folderNames.get(position)).get(0).getImgUri();
        Glide.with(context)
                .load(img)
                .error(R.drawable.error_image)
                .into(holder.folderImage);
        holder.folderImage.setOnClickListener(view -> {
            Intent intent = new Intent(context.getApplicationContext(), GalleryImages.class);
            intent.putParcelableArrayListExtra("images", folders.get(folderNames.get(position)));
            intent.putExtra("folder_name", folderNames.get(position));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }
}