/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Makes calls to the collection-image endpoint of the Rijksmuseum API
   See Rijksmuseum API documentation: http://rijksmuseum.github.io/*/

package nl.mprog.mume.Classes;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ImageRetriever {

    private Parser parser = new Parser();

    public ImageRetriever(){
       // constructor
    }


    // returns a JSON-array of all the levels of one image
    public void retrieveRMThumbnailArray(RequestQueue requestQueue, QueryMaker queryMaker,
                                         String imageid, final VolleyCallback callback){

        // make http GET request to get the response of the collection-image endpoint
        JsonObjectRequest imageurlrequest = new JsonObjectRequest(Request.Method.GET,
                queryMaker.getRequestURL(imageid),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        // If there is a successful response:
                        // parse the response
                        parser.parseRMImage(response);

                        // get the image LevelsArray from the parser
                        // resource: http://stackoverflow.com/questions/28120029
                        callback.onSuccess(parser.getRMImageLevelsarray());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // if the request failed:
                Log.e("VOLLEY", "There was an error in the response:" + error.getMessage());

            }
        });

        // add the request to the queue
        requestQueue.add(imageurlrequest);
    }


    // VolleyCallback to the Parser
    // resource: http://stackoverflow.com/questions/28120029
    public interface VolleyCallback{
        void onSuccess(JSONArray result);
    }


    // get the url of a imagetile of the lowest level that is at least 250x250 px (thus large
    // enough to be used as a thumbnail).
    // Resource: http://stackoverflow.com/questions/6695710
    public String getThumbnailURL(JSONArray responselevels){

        // there are seven levels. order them form small to large
        String[] levels = {"z6", "z5", "z4", "z3", "z2", "z1", "z0"};

        int levelslength = levels.length;
        int arraylength = responselevels.length();

        try {
            // Not every JSONArray (responselevels) has all the levels. The higher levels (z0 etc)
            // are always present. the lower levels (z6 or z5) are not.  We want the thumbnail to be
            // as small as possible so we need start checking if z6 (the smallest level) is present
            // in the JSONArray, if not we move on to try finding the next level (z5) etc..

            // loop through the array of levels ({"z6", "z5", "z4", "z3", "z2", "z1", "z0"})
            for (int j = 0; j < levelslength; j++){

                // loop through the response array and find the first (smallest) one
                for (int i = 0; i < arraylength; i++){
                    JSONObject currentLevel = responselevels.getJSONObject(i);

                    // we want the total size of the level to be at least 250x250 px to fill
                    // a thumbnail. If that is the case for this level::
                    if (Objects.equals(currentLevel.getString("name"), levels[j])
                            && currentLevel.getInt("width") > 250
                            && currentLevel.getInt("height") > 250){
                        // loop through the tiles
                        JSONArray tiles = currentLevel.getJSONArray("tiles");
                        int tilesLength = tiles.length();
                        for (int k = 0; k < tilesLength; k++){
                            // find the tile at position 0,0
                            // (the first tile, containing most of the image)
                            if (tiles.getJSONObject(k).getInt("x") == 0
                                    && tiles.getJSONObject(k).getInt("y") == 0){
                                // return it (end of method)
                                return tiles.getJSONObject(k).getString("url");
                            }
                        }
                    }
                    // else try the next level
                }
            }
            // if we have not found anything:
            return null;

        } catch (JSONException e){
            // Handle any possible errors
            Log.e("IMAGE RETRIEVER",
                    "There was an error in parsing for RM collection-image endpoint: "
                    + e.getMessage());
            return null;
        }
    }
}
