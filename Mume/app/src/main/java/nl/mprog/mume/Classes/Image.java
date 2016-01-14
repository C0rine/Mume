/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/*  */

package nl.mprog.mume.Classes;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class Image {

   private StringBuilder resultsbuilder;
   private String[] results;
   private Parser parser = new Parser();


   public Image(){
      // constructor
       resultsbuilder = new StringBuilder();
   }

   public StringBuilder getresultsbuilder(){
       return this.resultsbuilder;
   }


   /* public String[] getImageURLS(String[] imageids) {

       // create the request queue using Volley
       RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

       // Create query
       Searcher searcher = new Searcher();
       searcher.setSearchtype("image");


       // Loop trough the imageids
       for (String imageid : imageids) {

           String URL = retrieveURL(requestQueue, searcher, imageid, new VolleyCallback() {
               @Override
               public void onSuccess(String result) {
                   getresultsbuilder().append(result);
                   Log.e("RESULT", getresultsbuilder().toString());
               }
           });
       }

       // return String[]
       return results;

   } */

    public String retrieveURL(RequestQueue requestQueue, Searcher searcher, String imageid, final VolleyCallback callback){

        // make request to get the images
        JsonObjectRequest imageurlrequest = new JsonObjectRequest(Request.Method.GET, searcher.getRequestURL(imageid),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

                        // parse the response
                        parser.parseRMImage(response);

                        callback.onSuccess(parser.getRMimageURL());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // process any errors
                Log.e("VOLLEY", "There was an error in the response:" + error.getMessage());

            }
        });

        requestQueue.add(imageurlrequest);

        return "failure";
    }

    public interface VolleyCallback{
        void onSuccess(String result);
    }

}
