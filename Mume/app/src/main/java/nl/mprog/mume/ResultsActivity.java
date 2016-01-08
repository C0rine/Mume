/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* The activity that shows all search results*/

package nl.mprog.mume;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ResultsActivity extends AppCompatActivity {

    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // initialize the Adapter for the custom layout of the gridview
        gridview = (GridView) findViewById(R.id.results_gridview);
        gridview.setAdapter(new ResultsAdapter(this));

        // What to do when an item in the gridview gets clicked?:
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Show the position of the thumbnail the user clicked
                Toast.makeText(ResultsActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();

                // Open the next activity when the 9th (in this case last) item is clicked
                if (position == 9){
                    Intent showResult = new Intent(ResultsActivity.this, SelectedActivity.class);
                    startActivityForResult(showResult, 1);
                }
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


    // Implementation menu functionality
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
