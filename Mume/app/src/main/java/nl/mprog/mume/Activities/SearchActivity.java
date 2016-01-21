/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* First activity that gets shown to the user upon opening the app*/

package nl.mprog.mume.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
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

import java.util.Arrays;
import java.util.regex.Pattern;

import nl.mprog.mume.Adapters.FacebookImagesAdapter;
import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private CallbackManager callbackManager;

    private StringBuilder urlStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initializing the Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_search);

        urlStringBuilder = new StringBuilder();

        // set up the Recyclerview for the Facebook cardviews
        // resource: https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        // initialize the Facebook login button
        // resource: https://developers.facebook.com/docs/graph-api/reference/v2.5/album
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("FACEBOOK", "Login succesful");

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
                                        Log.e("FACEBOOK", "photo id " + Integer.toString(i) + " : " + currentId);

                                        // create the image url based on the id:
                                        String currentUrl = "http://graph.facebook.com/" + currentId + "/picture";
                                        Log.e("FACEBOOK", "photo url: " + currentUrl);

                                        // append the url to the stringbuilder
                                        urlStringBuilder.append(currentUrl + "\n");
                                    }

                                    // all urls have been found
                                    // convert the stringbuilder to a stringarry so it can be used in the layout
                                    String allurls = urlStringBuilder.toString();
                                    String[] urlarray = allurls.split(Pattern.quote("\n"));
                                    Log.e("FACEBOOK", "url array: " + Arrays.toString(urlarray));

                                    FacebookImagesAdapter fia = new FacebookImagesAdapter(getApplicationContext(), urlarray);
                                    recyclerView.setAdapter(fia);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("FACBOOOK", "failed to get the photos");
                                }
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("FACEBOOK", "Login cancelled");

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("FACEBOOK", "Login error");
            }
        });
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

        return super.onOptionsItemSelected(item);
    }
}
