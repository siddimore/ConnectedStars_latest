//package com.parse.ui;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.location.Location;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.facebook.FacebookException;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.model.GraphUser;
//import com.facebook.widget.ProfilePictureView;
//
///**
// * Created by Sid on 6/27/2015.
// */
//public class FacebookLogin {
//
//    private final String PENDING_ACTION_BUNDLE_KEY =
//            "com.example.hellofacebook:PendingAction";
//
//    private Button postStatusUpdateButton;
//    private Button postPhotoButton;
//    private ProfilePictureView profilePictureView;
//    private TextView greeting;
//    //private PendingAction pendingAction = PendingAction.NONE;
//    private boolean canPresentShareDialog;
//    private boolean canPresentShareDialogWithPhotos;
//    //private CallbackManager callbackManager;
////    private ProfileTracker profileTracker;
////    private ShareDialog shareDialog;
//    private Context thisContext;
//
//    void FacebookLogin(Context context)
//    {
//        thisContext = context;
//    }
//
//    private static final String PERMISSION = "publish_actions";
//    private static final Location SEATTLE_LOCATION = new Location("") {
//        {
//            setLatitude(47.6097);
//            setLongitude(-122.3331);
//        }
//    };
//
//
//    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
//        @Override
//        public void onCancel() {
//            Log.d("HelloFacebook", "Canceled");
//        }
//
//        @Override
//        public void onError(FacebookException error) {
//            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
//            String title = getString(R.string.error);
//            String alertMessage = error.getMessage();
//            showResult(title, alertMessage);
//        }
//
//        @Override
//        public void onSuccess(Sharer.Result result) {
//            Log.d("HelloFacebook", "Success!");
//            if (result.getPostId() != null) {
//                String title = getString(R.string.success);
//                String id = result.getPostId();
//                String alertMessage = getString(R.string.successfully_posted_post, id);
//                showResult(title, alertMessage);
//            }
//        }
//
//        private void showResult(String title, String alertMessage) {
//            new AlertDialog.Builder(thisContext)
//                    .setTitle(title)
//                    .setMessage(alertMessage)
//                    .setPositiveButton("Ok", null)
//                    .show();
//        }
//    };
//
//}
