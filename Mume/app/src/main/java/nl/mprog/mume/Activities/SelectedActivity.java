/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Shows one artrecord (image and metadata). Has a button to start editing the image */

package nl.mprog.mume.Activities;

import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aviary.android.feather.sdk.AviaryIntent;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.UUID;

import nl.mprog.mume.Classes.Parser;
import nl.mprog.mume.Classes.VolleySingleton;
import nl.mprog.mume.CustomView.TouchImageView;
import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;

public class SelectedActivity extends AppCompatActivity {

    private TextView artisname_holder;
    private TextView title_holder;
    private TextView dating_holder;
    private TextView materials_holder;
    private TouchImageView image_holder;
    private ScrollView scrollView;

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getmImageLoader();

        artisname_holder = (TextView) findViewById(R.id.artistnameholder_textview);
        title_holder = (TextView) findViewById(R.id.titleholder_textview);
        dating_holder = (TextView) findViewById(R.id.datingholder_textview);
        materials_holder = (TextView) findViewById(R.id.materialsholder_textview);
        image_holder = (TouchImageView) findViewById(R.id.artimage_imageview);
        scrollView = (ScrollView) findViewById(R.id.selected_scrollview);

        String dataUrl = getIntent().getStringExtra("dataUrl");

        imageUrl = getIntent().getStringExtra("imageUrl");
        image_holder.setImageUrl(imageUrl, imageLoader);

        // prevent scrolling of scrollview when the user uses the image to zoom
        image_holder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        // create the request queue using Volley
        RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

        // Request the results of the search with http GET request
        JsonObjectRequest collectionrequest = new JsonObjectRequest(Request.Method.GET, dataUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){

                        // handle the response of the request:
                        // parse the response
                        Parser parser = new Parser();
                        Log.e("Selected Response", response.toString());
                        parser.parseRMcollectiondetail(response);

                        // retrieve the parsed artistnames and object ids as a stringarray
                        String artistname = parser.getRMprincipalmakers();
                        String title = parser.getRMtitle();
                        String dating = parser.getRMdating();
                        String materials = parser.getRMmaterials();

                        artisname_holder.setText(artistname);
                        title_holder.setText(title);
                        dating_holder.setText(dating);
                        materials_holder.setText(materials);

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

                // handle any errors that might occur when trying to get the request
                Log.e("VOLLEY", "There was an error in the response to the collection-endpoint:" + error.getMessage());

            }
        });

        // add the request to the queue
        requestQueue.add(collectionrequest);

    }


    // Inflate menu facilitate help-button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }


    // Implementation of action-bar / menu functionalities
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


    // gets executed when the 'Meme it' button gets pressed
    public void startEdit(View view){
        // open the EditActivity to start editing the image
//        Intent startEdit = new Intent(this, EditActivity.class);
//        startActivityForResult(startEdit, 1);

        /* 2) Create a new Intent */
        Intent imageEditorIntent = new AviaryIntent.Builder(this)
                .setData(Uri.parse(imageUrl))
                .build();

        /* 3) Start the Image Editor with request code 1 */
        startActivityForResult(imageEditorIntent, 1);
    }

    // what to do when we are done with the image-editing in the aviary activity:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                /* 4) Make a case for the request code we passed to startActivityForResult() */
                case 1:

                    Uri mImageUri = data.getData();
                    try {
                        shareImage(mImageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    // method to open a share intent
    public void shareImage(Uri imageUri) throws FileNotFoundException {
        // get the uri of the image we want to send
//        Uri imageUri = Uri.parse("android.resource://nl.mprog.mume/mipmap/art_nightwatch");

        String imageName = UUID.randomUUID().toString() + ".jpg";

        // The receiving application needs permission to access the data the Uri points to
        // Use MediaStore for this
        String url = MediaStore.Images.Media.insertImage(getContentResolver(), imageUri.getPath(), imageName, "made with mume");

        // Opens a pop-up with all apps available to share an image
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        shareIntent.setType("image/jpg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.sendto_shareintent_title)));
    }
}

