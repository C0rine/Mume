/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* First activity that gets shown to the user upon opening the app*/

package nl.mprog.mume.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import nl.mprog.mume.Other.HelpDialog;
import nl.mprog.mume.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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


    // gets executed when the 'Go!' button gets pressed to perform the search
    public void startSearch(View view){
        // open new activity to show the results of the search
        Intent startSearch = new Intent(this, ResultsActivity.class);
        startActivityForResult(startSearch, 1);
    }
}
