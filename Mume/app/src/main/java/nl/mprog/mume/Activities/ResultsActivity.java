/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* The activity that shows all search results and performs the http GET request to
   get the results */

package nl.mprog.mume.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import nl.mprog.mume.Classes.Parser;
import nl.mprog.mume.Classes.QueryMaker;
import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;
import nl.mprog.mume.Adapters.ResultsAdapter;
import nl.mprog.mume.Classes.VolleySingleton;


public class ResultsActivity extends AppCompatActivity {

    private GridView gridview;
    private String[] artistnames;
    private String[] objectids;
    private ResultsAdapter resultsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        gridview = (GridView) findViewById(R.id.results_gridview);

        // get the intent and the data from the previous (search) activity
        Intent intent = getIntent();
        String searchwords = intent.getStringExtra("searchwords");

        // Building the query: we want to search the collection
        QueryMaker queryMaker = new QueryMaker();
        queryMaker.setSearchtype("collection");

        // create the request queue using Volley
        RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

        // Request the results of the search with http GET request
        JsonObjectRequest collectionrequest = new JsonObjectRequest(Request.Method.GET, queryMaker.getRequestURL(searchwords),
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){

                // handle the response of the request:
                // parse the response
                Parser parser = new Parser();
                parser.parseRMCollection(response);

                // retrieve the parsed artistnames and object ids as a stringarray
                artistnames = parser.getRMartistnames();
                objectids = parser.getRMobjectids();

                // set the parsed names and ids to the ResultsAdapter and set the adapter to the gridview
                // to display the results
                resultsAdapter = new ResultsAdapter(getApplicationContext(), artistnames, objectids);
                gridview.setAdapter(resultsAdapter);

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


        // What to do when an item in the gridview gets clicked?:
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Open the next activity when one item is clicked
                Intent showResult = new Intent(ResultsActivity.this, SelectedActivity.class);
                // send the objectnumber of the selected item along
                showResult.putExtra("objectid", resultsAdapter.getItem(position));
                startActivityForResult(showResult, 1);

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
