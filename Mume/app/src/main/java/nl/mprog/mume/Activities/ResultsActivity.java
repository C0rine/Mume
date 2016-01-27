/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* The activity that shows all search results and performs the http GET request to
   get the results */

package nl.mprog.mume.Activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import nl.mprog.mume.Classes.MyApplication;
import nl.mprog.mume.Classes.Parser;
import nl.mprog.mume.Classes.QueryMaker;
import nl.mprog.mume.Dialogs.HelpDialog;
import nl.mprog.mume.R;
import nl.mprog.mume.Adapters.ResultsAdapter;
import nl.mprog.mume.Classes.VolleySingleton;


public class ResultsActivity extends AppCompatActivity {

    private GridView gridview;
    private EditText searchbar;
    private LinearLayout dummyLinearLayout;
    private RelativeLayout loadingPanel;
    private TextView noresultstext;
    private ProgressBar progressBar;

    private String[] artistnames;
    private String[] objectids;
    private String[] bigImageUrls;

    private ResultsAdapter resultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        gridview = (GridView) findViewById(R.id.results_gridview);
        searchbar = (EditText) findViewById(R.id.searchbar_edittext);
        dummyLinearLayout = (LinearLayout) findViewById(R.id.dummy_linearlayout);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        noresultstext = (TextView) findViewById(R.id.noresults_textview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // initially hide the no-results text (we only want to show this when we are sure there
        // are no results)
        noresultstext.setVisibility(View.GONE);

        // Building the query: we want to search the collection
        final QueryMaker queryMaker = new QueryMaker();
        queryMaker.setSearchtype("collection");

        // get the Volley request queue to append networkrequests to
        final RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

        // get the intent and the data from the previous (search) activity
        Intent intent = getIntent();
        final String searchwords = intent.getStringExtra("searchwords");
        // perform a search (see method below) based on these searchwords and display them in
        // the layout
        performSearch(searchwords, queryMaker, requestQueue);

        // put the searchwords in the hint of the edit-text so the user knows what he/she
        // is searching
        searchbar.setHint(Html.fromHtml("<i><small>Searching for \'" + searchwords.trim()
                + "\'</small></i>"));


        // What to do when an item in the gridview gets clicked?:
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Open the next activity when one item is clicked
                Intent showResult = new Intent(ResultsActivity.this, SelectedActivity.class);
                // send the url for the detailed object info and the url for the image along with
                // the intent
                String url = resultsAdapter.getItem(position);
                String imageurl = resultsAdapter.getImage(position);
                showResult.putExtra("dataUrl", url);
                showResult.putExtra("imageUrl", imageurl);
                startActivityForResult(showResult, 1);
            }
        });

        // handle a new search (when clicked on search-icon (magnifying glass) in the searchbar)
        searchbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (searchbar.getRight() - searchbar.getCompoundDrawables()
                            [2].getBounds().width())) {
                        // perform the new search
                        performSearchFromSearchbar(queryMaker, requestQueue);
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
                    // perform the new search
                    performSearchFromSearchbar(queryMaker, requestQueue);
                    handled = true;
                }
                return handled;
            }
        });
    }


    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.general_menu, menu);
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


    // perform a search
    private void performSearch(String searchwords, QueryMaker queryMaker, RequestQueue requestQueue)
    {
        // for each search reset the following items:
        noresultstext.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        loadingPanel.animate().alpha(1f).setDuration(1);

        // Request the results of the search with http GET request
        JsonObjectRequest collectionrequest = new JsonObjectRequest(Request.Method.GET,
                queryMaker.getRequestURL(searchwords),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){

                        // handle the response of the request:
                        // parse the response
                        Parser parser = new Parser();
                        parser.parseRMCollection(response);

                        // retrieve the parsed artistnames and object ids as a String-array
                        artistnames = parser.getRMartistnames();
                        objectids = parser.getRMobjectids();
                        // also retrieve the image urls (high res)
                        bigImageUrls = parser.getRMbigImageUrl();

                        // set the parsed names and ids to the ResultsAdapter and set the
                        // adapter to the gridview
                        // to display the results
                        resultsAdapter = new ResultsAdapter(getApplicationContext(), artistnames,
                                objectids, bigImageUrls);
                        gridview.setAdapter(resultsAdapter);

                        if (objectids.length < 2){
                            // there are no results, notify this to the user with a textview
                            Log.e("RESULTS", "there are no results");
                            progressBar.setVisibility(View.GONE);
                            noresultstext.setVisibility(View.VISIBLE);
                        }
                        else{
                            // there are search results, shortly show a loading screen
                            progressBar.setVisibility(View.VISIBLE);
                            final Handler handler = new Handler();
                            // wait 1500 ms
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // slowly animate the loadingPanel away
                                    loadingPanel.animate().alpha(0f).setDuration(1000);
                                    loadingPanel.setClickable(false);
                                }
                            }, 1500);
                        }


                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

                // handle any errors that might occur when trying to get the request
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


    // when a search gets performed from the edittext in this activity, we also need to handle
    // this edittext (searchbar)
    public void performSearchFromSearchbar(QueryMaker queryMaker, RequestQueue requestQueue){

        // get the new searchwords from the edittext
        String newsearchwords = searchbar.getText().toString();

        // perform the new search
        performSearch(newsearchwords, queryMaker, requestQueue);

        // reset the edittext and show the current searchwords
        searchbar.setText("");
        // resource: http://stackoverflow.com/questions/3465576
        searchbar.setHint(Html.fromHtml("<i><small>Searching for \'" + newsearchwords.trim()
                + "\'</small></i>"));

        // get rid of the on screen keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchbar.getWindowToken(), 0);
        // remove focus away from the edittext again, to also get rid of the cursor
        dummyLinearLayout.requestFocus();

    }
}
