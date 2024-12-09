
# Ex2-android
## This project is the front end of the Android application of facebook.




## Run:
Build and run using ide

## adapters
### MyAdapter
This code implements a RecyclerView adapter for a social media app. It populates the RecyclerView with posts containing various content such as text, images, dates, and user interactions like likes, comments, editing, and sharing. It utilizes a custom interface (RecyclerViewInterface) to handle user actions on the posts, such as clicking, liking, deleting, editing, adding comments, and sharing. The adapter dynamically adjusts the view based on whether the post was made by the current user or not, displaying appropriate options accordingly.

### MyAdapterForComments
This code defines a RecyclerView adapter named MyAdapterForComments for displaying a list of comments in an Android application. It binds comment data to corresponding views, such as user name, message, date, and image, and sets up click listeners for various actions like liking a comment, deleting a comment (if allowed), editing a comment (if allowed), and sharing a comment. The adapter also applies animations to the views and handles interaction through a provided interface RecyclerViewInterface, allowing communication between the adapter and the parent activity or fragment.

## entities
### AddCommentActivity
This code defines an activity called AddCommentActivity in an Android application. Its purpose is to allow users to add a new comment. The activity receives data such as the user's first name and image from a previous activity (likely a post activity). Users can input their comment message and then submit it. Upon submission, the activity constructs an intent to return to the post activity, passing along the newly added comment data including the message, user's first name, date, and other relevant information. Additionally, users have the option to go back to the previous screen using a back button.

### AddPostActivity
This code defines an activity named AddPostActivity in an Android application. Its purpose is to enable users to create a new post by allowing them to input a message and attach an image from the gallery. The activity receives data such as the user's first name and image from a previous activity (a profile activity). Users can input their post message and select an image from the gallery. Upon submission, the activity constructs an intent to return to the post activity, passing along the newly added post data including the message, user's first name, date, and the selected image URI. Additionally, users have the option to go back to the previous screen using a back button. If no image is selected, the user is prompted with a toast message indicating that an image must be chosen before submitting the post.

### EditMessageActivity
This code defines an activity named EditMessageActivity in an Android application. Its purpose is to allow users to edit either a post or a comment. The activity receives data such as the user's first name, message content, title, and indicate whether it's a post or a comment from a previous activity (likely the post or comment activity). Users can modify the message content and, if it's a post, they have the option to change the attached image. Upon submission, the activity constructs an intent to return to the post activity, passing along the edited data including the message, user's first name, date, and the updated image URI if applicable. Additionally, users have the option to go back to the previous screen using a back button.

### Item
This code defines a Java class named Item, which serves as a model for items in a social media application. Each item can represent either a post or a comment made by a user. The class provides various constructors to accommodate different scenarios, such as posts with images, posts without images, comments with images, and comments without images. It encapsulates properties such as user name, email, message content, date, image (either as a resource ID or a URI), like count, whether it's liked by the user, whether it's posted by the current user, and whether it's a headline in comments. Additionally, the class includes getter and setter methods for accessing and modifying these properties. Overall, the Item class serves to represent individual posts and comments within the social media application.

### MainActivity
This code defines the main activity of a social media application. The activity serves as the entry point where users can log in with their email and password. Upon successful login, the activity redirects the user to the post activity, passing along user information such as the first name, email, and an indicator of the source of the login. If the login credentials provided by the user match the stored credentials, the user is directed to the post activity. Otherwise, an error message is displayed indicating that the password or email doesn't match. Additionally, the activity provides an option to navigate to the registration activity for new users to create an account.

### MyViewHolder
This code defines a custom RecyclerView ViewHolder named MyViewHolder, which is responsible for holding references to the views that represent individual items in a list displayed by a RecyclerView. The ViewHolder initializes and holds references to various views such as ImageView, TextView, and RelativeLayout, which represent different components of a social media post or comment, including user profile image, name, message, date, like button, delete button, add comment button, edit post button, share button, and post photo. These views are inflated from a layout XML file and accessed through their respective IDs. By holding references to these views, the ViewHolder optimizes the performance of the RecyclerView by reducing the number of findViewById calls during list scrolling.

### PostActivity
This code represents the PostActivity, which serves as the main activity for displaying posts and comments in a social media app. It includes functionalities like displaying user posts, adding new posts, viewing and adding comments, editing posts and comments, deleting posts and comments, and sharing posts. 
The activity utilizes RecyclerViews to display posts and comments efficiently. It fetches data from a JSON file to populate the posts and comments initially. Users can also search for posts using a search bar. Additionally, it supports features like toggling night mode, navigation drawer for menu options, and handling various user interactions such as clicking on posts, likes, edit buttons, and share buttons. The code is structured to handle different scenarios based on user actions, such as adding a new post, adding a comment, editing a post or comment, and navigating between views.

### RecyclerViewInterface
This code defines a Java interface named RecyclerViewInterface, which declares several methods for handling various user interactions with items in a RecyclerView. These methods include handling item clicks, like clicks, determining if a post/comment is made by the current user, deleting posts/comments, adding comments to posts, editing posts/comments, and sharing posts. Implementing this interface allows classes to define custom behavior for these interactions in RecyclerView adapters or other components.

### RegisterActivity
This code defines an activity named RegisterActivity, which allows users to register for a social media app. It includes functionalities for validating user inputs such as email, password, and username, as well as enabling users to upload a profile picture either from the device's gallery or by taking a photo with the camera. Upon successful registration, the user's information, including email, first name, password, and profile picture URI, is passed to the main activity for further processing.

### users.json
This JSON data represents sample user posts and comments for a social media app. The "Users" array contains information about users' posts, including their names, messages, email addresses, dates, and possibly profile images. The "Comments" array includes comments made by other users on the posts, with details such as the commenter's name, message, email, date, and the email of the post they commented on ("commentFor"). This data can be used to populate the social media app's interface with posts and comments for users to interact with.

