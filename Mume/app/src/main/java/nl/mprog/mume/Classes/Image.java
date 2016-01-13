/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/*  */

package nl.mprog.mume.Classes;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class Image {

   private StringBuilder urls;
   private StringBuilder resultsbuilder;
   private String[] results; //= {"hello", "this", "is", "a", "test"};

   public Image(){
      // constructor
   }

   public String[] getImageURLS(String[] imageids) {
       RequestQueue requestQueue = null;
       for (String imageid : imageids) {

           // create the request queue using Volley
           requestQueue = VolleySingleton.getInstance().getmRequestQueue();

           // Create query
           Searcher searcher = new Searcher();
           searcher.setSearchtype("image");

           // make request to get the images
           JsonObjectRequest imageurlrequest = new JsonObjectRequest(Request.Method.GET, searcher.getRequestURL(imageid),
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         // handle the response of the request:
                         // parse the response
                         Parser parser = new Parser();
                         parser.parseRMImage(response);

                         Log.e("PARSING TEST", "this is the response: " + response);
                         Log.e("PARSING TEST", "this is the parser.getRMimageURL(): " + parser.getRMimageURL());
                         //Toast.makeText(getApplicationContext(), "Hello world \n" + response, Toast.LENGTH_LONG).show();
                         //resultsbuilder.append(parser.getRMimageURL() + "\n");

                         //urls.append(theURL + "\n");
                         //String myString = urls.toString();
                         //results = myString.split(Pattern.quote("\n"));
                    }
                 }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                     // process any errors
                     Log.e("VOLLEY", "There was an error in the response:" + error.getMessage());

                }
           });

           requestQueue.add(imageurlrequest);

       }

       //String resultstring = resultsbuilder.toString();
       //results = resultstring.split(Pattern.quote("\n"));



       return results;
   }

}
