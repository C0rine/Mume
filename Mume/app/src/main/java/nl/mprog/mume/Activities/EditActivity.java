/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Activity that provides the image editing*/

package nl.mprog.mume.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aviary.android.feather.sdk.AviaryIntent;

import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;


public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        /* 1) Make a new Uri object (Replace this with a real image URI) */
        Uri imageUri = Uri.parse("http://img-9gag-fun.9cache.com/photo/a2mrRd9_460s.jpg");

        /* 2) Create a new Intent */
        Intent imageEditorIntent = new AviaryIntent.Builder(this)
                .setData(imageUri)
                .build();

        /* 3) Start the Image Editor with request code 1 */
        startActivityForResult(imageEditorIntent, 1);

    }


    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_menu, menu);
        return true;
    }


    // Implementation of menu functionality
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

    // Gets executed once the button "share" is pressed
    public void shareImage(View view) {
        shareImage();
    }

    public void shareImage(){
        // get the uri of the image we want to send
        Uri imageUri = Uri.parse("android.resource://nl.mprog.mume/mipmap/art_nightwatch");

        // Opens a pop-up with all apps available to share an image
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.sendto_shareintent_title)));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                /* 4) Make a case for the request code we passed to startActivityForResult() */
                case 1:

                    /* 5) Show the image! */
//                    Uri mImageUri = data.getData();
//                    mResultImageView.setImageURI(mImageUri);
                    Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show();

                    // save the image:
                    // share intent:
                    shareImage();

                    break;
            }
        }
    }

}
