/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Makes calls to the collection-image endpoint of the Rijksmuseum API. */

package nl.mprog.mume.Classes;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class ImageRetriever {

    private Parser parser = new Parser();

    public ImageRetriever(){
       // constructor
    }

    public String retrieveURL(String imageid, final VolleyCallback callback){

        QueryMaker queryMaker = new QueryMaker();
        queryMaker.setSearchtype("image");

        RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

        // make http GET request to get the reponse of the collection-image endpoint
        JsonObjectRequest imageurlrequest = new JsonObjectRequest(Request.Method.GET, queryMaker.getRequestURL(imageid),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

                        // parse the response
                        parser.parseRMImage(response);

                        // If there is a successful response, get the image URL from the parser
                        // resource: http://stackoverflow.com/questions/28120029
                        callback.onSuccess(parser.getRMimageURL());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // process any errors
                Log.e("VOLLEY", "There was an error in the response:" + error.getMessage());

            }
        });

        // add the request to the queue
        requestQueue.add(imageurlrequest);

        return "failure";
    }

    // resource: http://stackoverflow.com/questions/28120029
    public interface VolleyCallback{
        void onSuccess(String result);
    }

}
