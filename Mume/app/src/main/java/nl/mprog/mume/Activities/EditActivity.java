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

import nl.mprog.mume.Other.HelpDialog;
import nl.mprog.mume.R;


public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }


    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
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

        // get the uri of the image we want to send
        Uri imageUri = Uri.parse("android.resource://nl.mprog.mume/mipmap/art_nightwatch");

        // Opens a pop-up with all apps available to share an image
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.sendto_shareintent_title)));

    }

}
