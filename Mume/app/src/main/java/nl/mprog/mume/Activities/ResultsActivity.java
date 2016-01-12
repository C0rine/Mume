/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* The activity that shows all search results*/

package nl.mprog.mume.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import nl.mprog.mume.Classes.Searcher;
import nl.mprog.mume.Other.HelpDialog;
import nl.mprog.mume.R;
import nl.mprog.mume.Adapters.ResultsAdapter;
import nl.mprog.mume.Classes.VolleySingleton;


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

        String searchtype = "collection";

        // Building the query
        Searcher searcher = new Searcher(searchtype);
        searcher.createQuery("Rembrandt");


        // Test code for Volley
        final TextView mTextview = (TextView) findViewById(R.id.volleytest_textview);

        RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searcher.getRequesturl(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){

                // handle the response of the request
                mTextview.setText(response.toString());

                //parseJSONresponse(response);

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

                // handle any errors that might occur when trying to get the request
                mTextview.setText(error.getMessage());

            }
        });

        // add the request to the queue
        requestQueue.add(request);

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
