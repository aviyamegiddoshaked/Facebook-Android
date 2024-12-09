package com.example.taliSocialMedia;

// Interface defining methods for RecyclerView actions
public interface RecyclerViewInterface {
    // Method triggered when an item in the RecyclerView is clicked
    void onItemClick(int position);

    // Method triggered when the "like" button is clicked on an item
    boolean onLikeClick(int position);

    // Method to check if the post is made by the current user
    boolean checkIfPostIsMadeByCurrentUser(int position);

    // Method triggered when the "delete" button is clicked on an item
    void onDeleteClick(int position);

    // Method triggered when the "add comment" button is clicked on an item
    void onAddCommentClick(int position);

    // Method triggered when the "edit post" button is clicked on an item
    void onEditPostClick(int position);

    // Method triggered when the "share" button is clicked on an item
    void onShareClick(int position);
}
