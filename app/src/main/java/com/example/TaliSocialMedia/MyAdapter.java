package com.example.taliSocialMedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    // Interface for communicating click events with the activity
    private final RecyclerViewInterface recyclerViewInterface;

    // Context and list of items to display
    Context context;
    List<Item> items;

    // Constructor to initialize the adapter with context, list of items, and RecyclerViewInterface
    public MyAdapter(Context context, List<Item> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.items = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setItems(List<Item> newItems) {
        // Calculate the differences between old and new lists
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return items.size();
            }

            @Override
            public int getNewListSize() {
                return newItems.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return items.get(oldItemPosition).getName().equals(newItems.get(newItemPosition).getName());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return items.get(oldItemPosition).equals(newItems.get(newItemPosition));
            }
        });

        // Update the posts list and notify the adapter of changes
        items = newItems;
        diffResult.dispatchUpdatesTo(this);
    }

    // Method to set a filtered list of items
    public void setFilteredList(List<Item> filteredList){
        this.items = filteredList;
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void remoteItem(int position) {
        this.items.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the layout for each item view
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Binding data to each item view
        holder.nameView.setText(items.get(position).getName());
        holder.messageView.setText(items.get(position).getMessage());

        // Setting image based on whether it's a resource ID or a URI
        if (items.get(position).getImage() == 0) {
            Uri uriImage = items.get(position).getUriImage();
            if (uriImage != null) {
                String scheme = uriImage.getScheme();
                if (scheme != null && scheme.equals("data")) {
                    Bitmap bitmap = Helper.decodeImageFromBase64(uriImage);
                    holder.imageView.setImageBitmap(bitmap);
                } else {
                    holder.imageView.setImageURI(uriImage);
                }
            }
        }
        else
            holder.imageView.setImageResource(items.get(position).getImage());

        // Setting post photo if available
        Uri postPhoto = items.get(position).getPostPhoto();
        if (postPhoto != null) {
            String scheme = postPhoto.getScheme();
            if (scheme != null && scheme.equals("data")) {
                Bitmap bitmap = Helper.decodeImageFromBase64(postPhoto);
                holder.postPhoto.setImageBitmap(bitmap);
            } else {
                holder.postPhoto.setImageURI(items.get(position).getPostPhoto());
            }
        }

        // Setting date
        holder.dateView.setText(items.get(position).getDate());

        // Click listener for item click event
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewInterface != null) {
                    recyclerViewInterface.onItemClick(holder.getAdapterPosition());
                }
            }
        });

        // Click listener for like button
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean liked = recyclerViewInterface.onLikeClick(holder.getAdapterPosition());
                if (!liked) {
                    holder.like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                } else {
                    holder.like.setImageResource(R.drawable.baseline_thumb_up_pressed);
                }
            }
        });
        if (!items.get(position).isitliked) {
            holder.like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
        } else {
            holder.like.setImageResource(R.drawable.baseline_thumb_up_pressed);
        }

        // Checking if the post is made by the current user
        boolean postByUser = recyclerViewInterface.checkIfPostIsMadeByCurrentUser(holder.getAdapterPosition());
        if (postByUser) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.editPost.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
            holder.editPost.setVisibility(View.GONE);
        }

        // Click listener for delete button
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onDeleteClick(holder.getAdapterPosition());
            }
        });

        // Click listener for add comment button
        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onAddCommentClick(holder.getAdapterPosition());
            }
        });

        // Click listener for edit post button
        holder.editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onEditPostClick(holder.getAdapterPosition());
            }
        });

        // Click listener for share button
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onShareClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
