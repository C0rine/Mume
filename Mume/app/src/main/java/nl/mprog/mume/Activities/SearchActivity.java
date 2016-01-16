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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;

public class SearchActivity extends AppCompatActivity {

    private EditText searchbar;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
    // the search-icon the edittext gets pressed.
    public void startSearch(){
        // open new activity to show the results of the search
        Intent startSearch = new Intent(this, ResultsActivity.class);
        // send the searchwords along to the next activity
        startSearch.putExtra("searchwords", searchbar.getText().toString());
        startActivityForResult(startSearch, 1);
    }

    public void startSearch(View view) {
        startSearch();
    }
}
