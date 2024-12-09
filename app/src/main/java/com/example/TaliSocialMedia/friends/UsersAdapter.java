package com.example.taliSocialMedia.friends;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taliSocialMedia.Helper;
import com.example.taliSocialMedia.R;
import com.example.taliSocialMedia.network.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<User> users;
    private User currentUser;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position, boolean isPrimary);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    public void setUsers(List<User> newUsers, User currentUser) {
        this.currentUser = currentUser;
        // Calculate the differences between old and new lists
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return users.size();
            }

            @Override
            public int getNewListSize() {
                return newUsers.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return users.get(oldItemPosition).getName().equals(newUsers.get(newItemPosition).getName());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return users.get(oldItemPosition).equals(newUsers.get(newItemPosition));
            }
        });

        this.users = newUsers;
        diffResult.dispatchUpdatesTo(this);
    }

    public List<User> getUsers() {
        return users;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.userNameTextView.setText(user.getName());
        holder.friendSecondaryButton.setVisibility(View.GONE);
        holder.friendPrimaryButton.setEnabled(true);
        if (currentUser.getFriends().contains(user.getId())) {
            holder.friendPrimaryButton.setImageResource(R.drawable.baseline_delete_outline_24);
        } else if (currentUser.getFriendRequestsReceived().contains(user.getId())) {
            holder.friendPrimaryButton.setImageResource(R.drawable.baseline_check_24);
            holder.friendSecondaryButton.setImageResource(R.drawable.baseline_close_24);
            holder.friendSecondaryButton.setVisibility(View.VISIBLE);
        } else if (currentUser.getFriendRequestsSent().contains(user.getId())) {
            holder.friendPrimaryButton.setImageResource(R.drawable.baseline_call_made_24);
            holder.friendPrimaryButton.setEnabled(false);
        } else {
            holder.friendPrimaryButton.setImageResource(R.drawable.baseline_add_circle_24);
        }

        if (user.getImage() != null) {
            Uri uriImage = Uri.parse(user.getImage());
            if (uriImage != null) {
                String scheme = uriImage.getScheme();
                if (scheme != null && scheme.equals("data")) {
                    Bitmap bitmap = Helper.decodeImageFromBase64(uriImage);
                    holder.userImage.setImageBitmap(bitmap);
                } else {
                    holder.userImage.setImageURI(uriImage);
                }
            }
        } else {
            holder.userImage.setImageResource(R.drawable.a);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userNameTextView;
        CircleImageView userImage;
        ImageButton friendPrimaryButton;
        ImageButton friendSecondaryButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            friendPrimaryButton = itemView.findViewById(R.id.friend_button_one);
            friendPrimaryButton.setOnClickListener(this);
            friendSecondaryButton = itemView.findViewById(R.id.friend_button_two);
            friendSecondaryButton.setOnClickListener(this);
            userImage = itemView.findViewById(R.id.user_photo);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position, v == friendPrimaryButton);
                }
            }
        }
    }
}
