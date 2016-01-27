/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* First activity that gets shown to the user upon opening the app
   Shows an edittext that can be used to perform a search, and allows a user to login
   to Facebook to view the official Classical Art Memes images. */

package nl.mprog.mume.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import nl.mprog.mume.Adapters.FacebookImagesAdapter;
import nl.mprog.mume.Classes.MyApplication;
import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static LoginButton loginButton;

    private CallbackManager callbackManager;

    private StringBuilder urlStringBuilder;
    private StringBuilder timestampStringBuilder;
    private StringBuilder nameBuilder;
    private StringBuilder postUrlStringBuilder;

    private String[] emptyArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initializing the Facebook SDK (this needs to be the first thing done in onCreate)
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_search);

        urlStringBuilder = new StringBuilder();
        timestampStringBuilder = new StringBuilder();
        nameBuilder = new StringBuilder();
        postUrlStringBuilder = new StringBuilder();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        // initialize the Facebook login button
        // resource: https://developers.facebook.com/docs/graph-api/reference/v2.5/album
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FACEBOOK", "Login successful");
                // if the login is successful we want to get and display the FB images
                getFBimages();
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK", "Login cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("FACEBOOK", "Login error:" + exception.getMessage());
                Toast.makeText(MyApplication.getAppContext(), R.string.failfacebooklogin_toast_text, Toast.LENGTH_LONG).show();
            }
        });
        // hide the login button (we handle the functionality of the button in the menu of the actionbar)
        loginButton.setVisibility(View.GONE);

        // if the user was not logged-in or did not just login, then send empty arrays to the adapter to make sure no cards are displayed
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        FacebookImagesAdapter fia = new FacebookImagesAdapter(getApplicationContext(), emptyArray, emptyArray, emptyArray, emptyArray);
        recyclerView.setAdapter(fia);

        // set up the Recyclerview for the Facebook cardviews
        // resource: https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        if (isFBLoggedIn()){
            // the user is logged in to Facebook, get and display the FB images
            getFBimages();

        }

        // set listener to detect user FB logout
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    // user logged out, reset the UI to not displaying the FB images anymore
                    FacebookImagesAdapter fia = new FacebookImagesAdapter(getApplicationContext(), emptyArray, emptyArray, emptyArray, emptyArray);
                    recyclerView.setAdapter(fia);
                }
            }
        };
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // used by FB login button:
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }


    // Implementation of action-bar / menu functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.searchhelp_menubutton) {
            // Open Help-dialog once the button gets pressed
            DialogFragment newFragment = new HelpDialog();
            newFragment.show(getFragmentManager(), "help");
            return true;
        }
        else if (id == R.id.searchFB_menubutton && isFBLoggedIn()){
            // Open FB logout Dialog when the user presses the FB button when he/she is currently logged in
            Log.e("FB", "user is logged in");
            DialogFragment newFragment = new FacebookLogoutDialog();
            newFragment.show(getFragmentManager(), "logout");
            return true;
        }
        else if (id == R.id.searchFB_menubutton && !isFBLoggedIn()){
            // Open FB login Dialog when the user presses the FB button when he/she is currently logged out
            Log.e("FB", "user is logged out");
            DialogFragment newFragment = new FacebookLoginDialog();
            newFragment.show(getFragmentManager(), "login");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // return true or false on whether the user is logged in on Facebook
    public boolean isFBLoggedIn(){
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token != null;
    }

    // get the FB images and send them to the adapter to be displayed in the activity
    public void getFBimages(){
        if (isOnline()){
            // there is internet connection
            // make API call to Facebook to get the Classical Art Memes photo album
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/595162167262642/photos",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            // get the object that contains the photos
                            JSONObject responseObject = response.getJSONObject();
                            try {
                                // find the array with the images and metadata in the response
                                JSONArray photosArray = responseObject.getJSONArray("data");

                                // loop through this array
                                int arraylength = photosArray.length();
                                for (int i = 0; i < arraylength; i++){
                                    // get the photo ids
                                    String currentId = photosArray.getJSONObject(i).getString("id");
                                    // create the image url based on the id:
                                    String currentUrl = "http://graph.facebook.com/" + currentId + "/picture";
                                    // append the url to the StringBuilder
                                    urlStringBuilder.append(currentUrl + "\n");

                                    // use the current id to get a posturl (directing to the post on FB)
                                    String currentPosturl = "http://facebook.com/" + currentId;
                                    // append this to other StringBuilder
                                    postUrlStringBuilder.append(currentPosturl + "\n");


                                    // get the photo timestamp
                                    String currentTimestamp = photosArray.getJSONObject(i).getString("created_time");
                                    // convert it to a easy human readable string (res: http://stackoverflow.com/questions/6882896/)
                                    String date =  GetLocalDateStringFromUTCString(currentTimestamp);
                                    // append the date to the stringbuilder
                                    timestampStringBuilder.append(date + "\n");

                                    // get the name/image caption (if there is one)
                                    try {
                                        JSONObject currentNameObject = photosArray.getJSONObject(i);
                                        String theName = currentNameObject.getString("name");
                                        // append if found
                                        nameBuilder.append(theName + "\n");
                                    } catch (JSONException e){
                                        // append just a space when there is none
                                        nameBuilder.append(" \n");
                                    }
                                }

                                // convert the stringbuilders to a stringarrays so it can be used in the layout
                                String allurls = urlStringBuilder.toString();
                                String[] urlarray = allurls.split(Pattern.quote("\n"));

                                String allposturls = postUrlStringBuilder.toString();
                                String[] posturlarray = allposturls.split(Pattern.quote("\n"));

                                String alldates = timestampStringBuilder.toString();
                                String[] datesarray = alldates.split(Pattern.quote("\n"));

                                String allnames = nameBuilder.toString();
                                String[] namesarray = allnames.split(Pattern.quote("\n"));

                                // send the string[]s to the adapter and set the adapter on the recyclerview
                                FacebookImagesAdapter fia = new FacebookImagesAdapter(getApplicationContext(), urlarray, posturlarray, datesarray, namesarray);
                                recyclerView.setAdapter(fia);

                            } catch (JSONException e) {
                                // something went wrong in the getting the request
                                e.printStackTrace();
                                Log.e("FACEBOOK", "failed to get the photos");
                            }
                        }
                    }
            ).executeAsync();
        }
        else {
            // there is no internet connection
            // use empty arrays
            FacebookImagesAdapter fia = new FacebookImagesAdapter(getApplicationContext(), emptyArray, emptyArray, emptyArray, emptyArray);
            recyclerView.setAdapter(fia);
            // notify no-internet error to user by showing a toast
            Toast.makeText(this, R.string.nointernet_toast_text, Toast.LENGTH_LONG).show();
        }
    }


    // convert the date we get from Facebook to a better readable date
    // resource: http://stackoverflow.com/questions/8734932/
    public String GetLocalDateStringFromUTCString(String utcLongDateTime) {
        SimpleDateFormat fb_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZZ");
        SimpleDateFormat my_dateFormat = new SimpleDateFormat("dd-MM-yyyy  HH:mm");

        // initialize string to empty string so in case the parsing and formatting fails at least something can be returned
        String localDateString = "";

        // reformat the date to better readable one and time and return it
        long when = 0;
        try {
            when = fb_dateFormat.parse(utcLongDateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        localDateString = my_dateFormat.format(new Date(when));
        return localDateString;
    }


    // needs to be inner class to use the login-button
    // dialog that gets shown when the user wants to login to FB
    public static class FacebookLoginDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // set the necessary data for the dialog
            builder.setTitle(R.string.facebooklogin_dialog_title);
            builder.setMessage(R.string.facebooklogin_dialog_message);
            builder.setNegativeButton(R.string.facebooklogin_dialog_negativebutton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    // Android automatically closes the dialog when this button is pressed
                }
            });
            builder.setPositiveButton(R.string.facebooklogin_dialog_positivebutton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User wants to login
                    loginButton.performClick();
                }
            });

            return builder.create();
        }

    }


    // needs to be inner class to use the login-button
    // dialog that gets shown when the user wants to logout of FB
    public static class FacebookLogoutDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // set the necessary data for the dialog
            builder.setTitle(R.string.facebooklogout_dialog_title);
            builder.setMessage(R.string.facebooklogout_dialog_message);
            builder.setNegativeButton(R.string.facebooklogout_dialog_negativebutton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    // Android automatically closes the dialog when this button is pressed
                }
            });
            builder.setPositiveButton(R.string.facebooklogout_dialog_positivebutton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User wants to login
                    loginButton.performClick();
                }
            });

            return builder.create();
        }

    }

    // check if there is a internet connection, return answer as boolean
    // resource: http://stackoverflow.com/questions/1560788/
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
