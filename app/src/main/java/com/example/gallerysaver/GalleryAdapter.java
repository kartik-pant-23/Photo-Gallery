package com.example.gallerysaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Image> images;

    public GalleryAdapter(Context context, ArrayList<Image> images) {
        this.context=context;
        this.images=images;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView galleryImg, check;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            galleryImg = itemView.findViewById(R.id.galleryImg);
            check = itemView.findViewById(R.id.check);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_image, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(images.get(position).getImgUri())
                .error(R.drawable.error_image)
                .into(holder.galleryImg);
        holder.galleryImg.setOnClickListener(view -> {
            if(itemClickListener!=null) {
                Image image = images.get(position);
                image.setChecked(!image.isChecked());
                itemClickListener.onItemClickListener(image);
                holder.check.setVisibility((image.isChecked() ?View.VISIBLE :View.GONE));
                holder.galleryImg.setBackgroundColor((image.isChecked()) ?0xFF03DAC5 :0x00FFFFFF);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    public interface ItemClickListener {
        void onItemClickListener(Image image);
    }

    private ItemClickListener itemClickListener;

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        itemClickListener=onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

}
