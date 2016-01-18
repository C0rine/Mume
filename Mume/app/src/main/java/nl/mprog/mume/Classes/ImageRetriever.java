/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Makes calls to the collection-image endpoint of the Rijksmuseum API.
   Can also merge multiple tiles of a bitmap image to one image */

package nl.mprog.mume.Classes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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

    // returns an JSON-array of all the levels of one image
    public void retrieveRMThumbnailArray(RequestQueue requestQueue, QueryMaker queryMaker, String imageid, final VolleyCallback callback){

        // make http GET request to get the reponse of the collection-image endpoint
        JsonObjectRequest imageurlrequest = new JsonObjectRequest(Request.Method.GET, queryMaker.getRequestURL(imageid),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

                        // parse the response
                        parser.parseRMImage(response);

                        // If there is a successful response, get the image LevelsArray from the parser
                        // resource: http://stackoverflow.com/questions/28120029
                        callback.onSuccess(parser.getRMImageLevelsarray());

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
    }

    // resource: http://stackoverflow.com/questions/28120029
    public interface VolleyCallback{
        void onSuccess(JSONArray result);
    }

    // uses multiple urls to request tiles and merges these to one image
    // Resource: http://stackoverflow.com/questions/6695710
    public String getThumbnailURL(JSONArray levelsarray){

        String[] levels = {"z6", "z5", "z4", "z3", "z2", "z1", "z0"};
        int levelslength = levels.length;
        int arraylength = levelsarray.length();

        try {

            // loop through the array of level
            for (int j = 0; j < levelslength; j++){

                // get the separate levels from the array

                // loop through the string array of level names and find the first (smallest) one
                // that has a minimum height and width

                for (int i = 0; i < arraylength; i++){
                    JSONObject currentLevel = levelsarray.getJSONObject(i);
                    Log.e("THUMBNAIL", "currentlevel name: " + currentLevel.get("name"));
                    Log.e("THUMBNAIL", "levels[j]: " + levels[j]);
                    Log.e("THUMBNAIL", "currentlevel width: " + currentLevel.getInt("width"));

                    // we want the total size of the level to be at least 350x350 px to fill a thumbnail
                    if (Objects.equals(currentLevel.getString("name"), levels[j]) && currentLevel.getInt("width") > 350
                            && currentLevel.getInt("height") > 350){
                        Log.e("THUMBNAIL", "we find level: " + levels[j]);
                        // loop throught the tiles and find the one at position 0,0
                        JSONArray tiles = currentLevel.getJSONArray("tiles");
                        int tilesLength = tiles.length();
                        for (int k = 0; k < tilesLength; k++){
                            if (tiles.getJSONObject(k).getInt("x") == 0 && tiles.getJSONObject(k).getInt("y") == 0){
                                return tiles.getJSONObject(k).getString("url");
                            }
                        }
                    }
                }
            }

            return null;

        } catch (JSONException e){
            // Handle any possible errors
            Log.e("PARSER", "There was an error in parsing for RM collection-image endpoint: "
                    + e.getMessage());

            return null;
        }

    }

}
