/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Shows one artrecord (image and metadata) based on the data send over from results-activity.
   Has a button to start editing the image */

package nl.mprog.mume.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.filters.ToolLoaderFactory;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.UUID;

import nl.mprog.mume.Classes.MyApplication;
import nl.mprog.mume.Classes.Parser;
import nl.mprog.mume.Classes.VolleySingleton;
import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;

public class SelectedActivity extends AppCompatActivity {

    private TextView artisname_holder;
    private TextView title_holder;
    private TextView dating_holder;
    private TextView materials_holder;
    private Button memeit_button;
    private NetworkImageView image_holder;
    private ScrollView scrollView;

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    private String dataUrl;
    private String imageUrl;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        // check if Aviary needs an update. Adobe recommends to put this here
        // resource: https://creativesdk.adobe.com/docs/android/#/articles/imageediting/index.html
        Intent intent = AviaryIntent.createCdsInitIntent(getBaseContext());
        startService(intent);

        // Volley and imageloader are to make network requests to get data and load images from url
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getmImageLoader();

        artisname_holder = (TextView) findViewById(R.id.artistnameholder_textview);
        title_holder = (TextView) findViewById(R.id.titleholder_textview);
        dating_holder = (TextView) findViewById(R.id.datingholder_textview);
        materials_holder = (TextView) findViewById(R.id.materialsholder_textview);
        memeit_button = (Button) findViewById(R.id.memeit_button);
        image_holder = (NetworkImageView) findViewById(R.id.artimage_imageview);
        scrollView = (ScrollView) findViewById(R.id.selected_scrollview);

        // get the urls for the image and data from the intent
        dataUrl = getIntent().getStringExtra("dataUrl");
        imageUrl = getIntent().getStringExtra("imageUrl");
        // check if there actually is a imageUrl
        if(imageUrl != null){
            // there is an image. Set it in the view:
            image_holder.setImageUrl(imageUrl, imageLoader);
        }
        else{
            // there was no image. This is due to copyright reasons.
            image_holder.setDefaultImageResId(R.mipmap.image_notavailable_icon);
            Toast.makeText(this, R.string.copyrightwarning_toast_text, Toast.LENGTH_LONG).show();
            //disable the button to meme the image
            memeit_button.setClickable(false);
        }


        // create the request queue using Volley
        RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

        // Get the metadata on the artobject using the dataurl in a http GET request
        JsonObjectRequest collectionrequest = new JsonObjectRequest(Request.Method.GET, dataUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){

                        // handle the response of the request:
                        // parse the response
                        Parser parser = new Parser();
                        parser.parseRMcollectiondetail(response);

                        // retrieve the parsed data as strings
                        String artistname = parser.getRMprincipalmakers();
                        String title = parser.getRMtitle();
                        String dating = parser.getRMdating();
                        String materials = parser.getRMmaterials();

                        // set the strings in their appropriate views
                        artisname_holder.setText(artistname);
                        title_holder.setText(title);
                        dating_holder.setText(dating);
                        materials_holder.setText(materials);

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

                // handle any errors that might occur when trying to get the response
                Log.e("VOLLEY", "There was an error in the response to the collection-endpoint:"
                        + error.getMessage());

                // if the error is caused because there is no internet connection, then notify
                // the user of this with a toast
                // resource: http://stackoverflow.com/questions/21011279/
                if(error instanceof NoConnectionError) {
                    Toast.makeText(MyApplication.getAppContext(), R.string.nointernet_toast_text,
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        // add the request to the queue
        requestQueue.add(collectionrequest);

    }


    // Inflate menu facilitate help-button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_menu, menu);
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
    // opens the Adobe Creative SDK (Aviary) activity for image editing with an intent
    public void startEdit(View view){

        // set the tools for the editor
        ToolLoaderFactory.Tools[] mTools = {ToolLoaderFactory.Tools.TEXT,
                ToolLoaderFactory.Tools.DRAW, ToolLoaderFactory.Tools.CROP,
                ToolLoaderFactory.Tools.MEME};

        // create the Intent
        Intent imageEditorIntent = new AviaryIntent.Builder(this)
                // send the imageurl along to retrieve the image in the editor
                .setData(Uri.parse(imageUrl))
                .withToolList(mTools)
                .build();

        // start the activity, 1 stands for what we want to do with the result of this activity
        // (see onActivityResult)
        startActivityForResult(imageEditorIntent, 1);
    }


    // what to do when we are done with the image-editing in the Aviary / Adobe Creative SDK:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                // Make a case for the request code we passed to startActivityForResult()
                case 1:
                    imageUri = data.getData();
                    // Ask the user if he/she wants to just save the image or also share it:
                    DialogFragment newFragment = new ShareSaveDialog();
                    newFragment.show(getFragmentManager(), "sharesave");
                    break;
            }
        }
    }


    // method to open a share intent
    public void shareImage(Uri imageUri) throws FileNotFoundException {

        String imageName = UUID.randomUUID().toString() + ".jpg";

        // The receiving application needs permission to access the data the Uri points to
        // We save the image using mediastore to achieve this
        String url = MediaStore.Images.Media.insertImage(getContentResolver(), imageUri.getPath(),
                imageName, "Made with mume");

        // Opens a pop-up with all apps available to share an image
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        shareIntent.setType("image/jpg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.sendto_shareintent_title)));
    }


    // dialog needs to be innerclass to use the response of the Adobe SDK
    @SuppressLint("ValidFragment")
    public class ShareSaveDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.sharesave_dialog_title);
            builder.setMessage(R.string.sharesave_dialog_message);
            builder.setNegativeButton(R.string.sharesave_dialog_negativebutton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    // Android automatically closes the dialog on the button press
                    // the image already got saved automatically by the Adobe SDK
                }
            });
            builder.setPositiveButton(R.string.sharesave_dialog_positivebutton,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                // try to share the image using a share-intent
                                shareImage(imageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Log.e("ADOBE", "Could not share the image. Error message: "
                                        + e.getMessage());
                            }
                        }
                    });
            return builder.create();
        }
    }
}

