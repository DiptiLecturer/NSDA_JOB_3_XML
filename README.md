NSDA Job 3 MVVM
1. Create a new Android Studio project and name it LocationSharingApp
2. Create a new Firebase Project and connect Android app to Firebase
3. Enable Authentication (Email and Password) and Cloud Firestore in your
Firebase Project.
4. Change Cloud Firestore Database rules, so that no one can access
databases without authentication.
5. Add necessary dependencies-Firebase Authentication, Cloud Firestore,
Google Play Services for Map and Location
6. Add necessary permissions for location detection in Manifest file
7. Get Map API key from cloud console and add the key as meta data in
Manifest file
8. Create an AuthActivity, this will be your launcher Activity.
9. Create a FriendListActivity. Later, you have to show all your friends in a
RecyclerView.

10. Create a MyProfileActivity. Here user (who is currently logged in)
information (display name, email, latitude, longitude) will be shown
11. Create a GoogleMapActivity. Here you will show all users with markers later.
12. Login or Register (if using this app for the first time) with Email address and
Password.
13. After successful login/register, the location detection process will start.
14. Get the device&#39;s current location/last location using the
FusedLocationProviderClient API and retrieve the Latitude and
Longitude.
15. Create a Kotlin Data Class, name it as AppUser and in the primary
constructor, declare the following parameters - userId, userEmail,
displayName (nullable), latitude, longitude
16. Create an AppUser object by providing all the information in the constructor.
17. Save AppUser to cloud firestore under AppUsers collection.
18. Go to FriendListActivity after saving the user.
19. In FriendListActivity, show all users in a RecyclerView. Each
RecyclerView item should show the User Name and Email Address. If User
Name is Null, show a default Text.
20. Create a Floating Options Menu, add three menu items - My Profile, Show
users on Map and Logout.
21. Clicking My Profile menu item should take the user to My Profile page. Show
the logged in user information in My Profile page.
22. Provide a button to update the display name (which will be initially null) in
My Profile page.
23. Clicking Show Users on Map menu item should take the user to Google
Map Screen where all the users will be shown on map as Marker. By clicking
the marker, show the user name.
24. Clicking the Logout menu item should take the user back to the Login
Screen.
