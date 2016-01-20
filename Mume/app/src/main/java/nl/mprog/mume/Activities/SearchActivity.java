/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* First activity that gets shown to the user upon opening the app*/

package nl.mprog.mume.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;

public class SearchActivity extends AppCompatActivity {

    private EditText searchbar;
    private Button startButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initializing the Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_search);


        // initialize the Facebook login button
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("FACEBOOK", "Login succesful");
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

        // make API call to Facebook to get the Classical Art Memes photo album
//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/595162167262642",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        Log.e("FACEBOOK", "album response: " + response.toString());
//                    }
//                }
//        ).executeAsync();




        searchbar = (EditText) findViewById(R.id.searchbar_edittext);
        startButton = (Button) findViewById(R.id.startsearch_button);

        // handle a press on the search-icon in the searchbar edittext
        // resource: http://stackoverflow.com/questions/3554377
        searchbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (searchbar.getRight() - searchbar.getCompoundDrawables()
                            [2].getBounds().width())) {
                        // start the search in the results-activity (see method below)
                        startSearch();
                        return true;
                    }
                }
                return false;
            }
        });

        // handle a press on the 'Go' button in the on screen keyboard
        // resource: http://developer.android.com/training/keyboard-input/style.html
        searchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    startSearch();
                    handled = true;
                }
                return handled;
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


    // gets executed when the 'Go!' button gets pressed to perform the search or when
    // the search-icon the edittext gets pressed, or when the 'go' button on the
    // on-screen keyboard gets pressed.
    public void startSearch(){

        // open new activity to show the results of the search
        Intent startSearch = new Intent(this, ResultsActivity.class);
        // send the searchwords along to the next activity
        startSearch.putExtra("searchwords", searchbar.getText().toString());
        startActivityForResult(startSearch, 1);

        // empty the edittext (just show a hint)
        searchbar.setText("");
    }

    // handle a press on the 'Go!' button in the layout of the activity
    public void startSearch(View view) {
        startSearch();
    }
}
