/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* First activity that gets shown to the user upon opening the app*/

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
        // Initializing the Facebook SDK
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
        // hide the login button
        loginButton.setVisibility(View.GONE);

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
            // the user is logged in to Facebook
            getFBimages();

        }

        // set listener to detect user logout
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    // user logged out, reset the UI
                    FacebookImagesAdapter fia = new FacebookImagesAdapter(getApplicationContext(), emptyArray, emptyArray, emptyArray, emptyArray);
                    recyclerView.setAdapter(fia);
                }
            }
        };

    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

        // Open Help-dialog once the button gets pressed
        if (id == R.id.searchhelp_menubutton) {

            DialogFragment newFragment = new HelpDialog();
            newFragment.show(getFragmentManager(), "help");
            return true;

        }
        else if (id == R.id.searchFB_menubutton && isFBLoggedIn()){
            Log.e("FB", "user is logged in");
            DialogFragment newFragment = new FacebookLogoutDialog();
            newFragment.show(getFragmentManager(), "logout");
            return true;
        }
        else if (id == R.id.searchFB_menubutton && !isFBLoggedIn()){
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

    public void getFBimages(){
        if (isOnline()){
            // make API call to Facebook to get the Classical Art Memes photo album
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/595162167262642/photos",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.e("FACEBOOK", "album response: " + response.toString());

                            // get the object that contains the photos
                            JSONObject responseObject = response.getJSONObject();
                            try {
                                // find the photo array in the response
                                JSONArray photosArray = responseObject.getJSONArray("data");
                                Log.e("FACEBOOK", "photosArray: " + photosArray.toString());

                                // loop through the photo array
                                int arraylength = photosArray.length();
                                for (int i = 0; i < arraylength; i++){
                                    // get the photo ids
                                    String currentId = photosArray.getJSONObject(i).getString("id");
                                    // create the image url based on the id:
                                    String currentUrl = "http://graph.facebook.com/" + currentId + "/picture";
                                    // append the url to the stringbuilder
                                    urlStringBuilder.append(currentUrl + "\n");
                                    // also use the current id to get a posturl
                                    String currentPosturl = "http://facebook.com/" + currentId;
                                    postUrlStringBuilder.append(currentPosturl + "\n");


                                    // get the photo timestamp
                                    String currentTimestamp = photosArray.getJSONObject(i).getString("created_time");
                                    // convert it to a human readable string (res: http://stackoverflow.com/questions/6882896/)
                                    String date =  GetLocalDateStringFromUTCString(currentTimestamp);
                                    // append the date to the stringbuilder
                                    timestampStringBuilder.append(date + "\n");

                                    try {
                                        // get the name (if there is one)
                                        JSONObject currentNameObject = photosArray.getJSONObject(i);
                                        String theName = currentNameObject.getString("name");
                                        // append if found, else append empty string
                                        nameBuilder.append(theName + "\n");
                                    } catch (JSONException e){
                                        nameBuilder.append(" \n");
                                    }

                                }

                                // all urls have been found
                                // convert the stringbuilders to a stringarrays so it can be used in the layout
                                String allurls = urlStringBuilder.toString();
                                String[] urlarray = allurls.split(Pattern.quote("\n"));

                                String allposturls = postUrlStringBuilder.toString();
                                String[] posturlarray = allposturls.split(Pattern.quote("\n"));

                                String alldates = timestampStringBuilder.toString();
                                String[] datesarray = alldates.split(Pattern.quote("\n"));

                                String allnames = nameBuilder.toString();
                                String[] namesarray = allnames.split(Pattern.quote("\n"));

                                FacebookImagesAdapter fia = new FacebookImagesAdapter(getApplicationContext(), urlarray, posturlarray, datesarray, namesarray);
                                recyclerView.setAdapter(fia);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("FACEBOOOK", "failed to get the photos");
                            }
                        }
                    }
            ).executeAsync();
        }
        else {
            // there is no internet connection
            FacebookImagesAdapter fia = new FacebookImagesAdapter(getApplicationContext(), emptyArray, emptyArray, emptyArray, emptyArray);
            recyclerView.setAdapter(fia);
            Toast.makeText(this, R.string.nointernet_toast_text, Toast.LENGTH_LONG).show();
        }
    }

    // resource: http://stackoverflow.com/questions/8734932/
    public String GetLocalDateStringFromUTCString(String utcLongDateTime) {
        SimpleDateFormat fb_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZZ");
        SimpleDateFormat my_dateFormat = new SimpleDateFormat("dd-MM-yyyy  HH:mm");

        String localDateString = null;

        long when = 0;
        try {
            when = fb_dateFormat.parse(utcLongDateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        localDateString = my_dateFormat.format(new Date(when + TimeZone.getDefault().getRawOffset() + (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0)));

        return localDateString;
    }


    // needs to be inner class to use the login-button
    public static class FacebookLoginDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Facebook Login");
            builder.setMessage("Login with Facebook to see the latest memes from the official \"Classical Art Memes\" Facebook page");
            builder.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    // Android automatically closes the dialog when this button is pressed
                }
            });
            builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User wants to login
                    loginButton.performClick();
                }
            });

            return builder.create();
        }

    }

    // needs to be inner class to use the login-button
    public static class FacebookLogoutDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Facebook Logout");
            builder.setMessage("Would you like to log out of Facebook?");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    // Android automatically closes the dialog when this button is pressed
                }
            });
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User wants to login
                    loginButton.performClick();
                }
            });

            return builder.create();
        }

    }

    // resource: http://stackoverflow.com/questions/1560788/
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
