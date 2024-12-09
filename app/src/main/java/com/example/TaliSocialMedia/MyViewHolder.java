package com.example.taliSocialMedia;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    // Views in the item view layout
    ImageView imageView, like, delete, addComment, editPost, share, postPhoto;
    TextView nameView, messageView, dateView;
    RelativeLayout relativeLayout;

    // Constructor to initialize views
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        // Finding views by their IDs in the item view layout
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);
        messageView = itemView.findViewById(R.id.message);
        dateView = itemView.findViewById(R.id.date);
        like = itemView.findViewById(R.id.like);
        delete = itemView.findViewById(R.id.delete);
        addComment = itemView.findViewById(R.id.addComment);
        editPost = itemView.findViewById(R.id.editPost);
        share = itemView.findViewById(R.id.share);
        postPhoto = itemView.findViewById(R.id.imageForPost);
        relativeLayout = itemView.findViewById(R.id.mainRelativeLayout);
    }
}
