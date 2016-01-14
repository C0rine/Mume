/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Parses the data retrieved by Volley and Searcher
   Resources: https://www.youtube.com/watch?v=5GzVtP0IODU&list=PLonJJ3BVjZW6CtAMbJz1XD8ELUs1KXaTD&index=37 */

package nl.mprog.mume.Classes;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class Parser {

    private StringBuilder names = new StringBuilder();
    private StringBuilder ids = new StringBuilder();
    private String result;

    public Parser(){

        //constructor

    }


    public void parseRMCollection(JSONObject response){

        // parses a Rijksmuseum collection-endpoint request
        // check if there actually is a response
        if(response == null || response.length() == 0){
            // there was no response
            Log.e("JSON", "There was no response from the JSONrequest or the request was " +
                    "empty.");
            return;
        }

        try {
            // find the array that contains the artobjects
            JSONArray artarray = response.getJSONArray("artObjects");

            // loop through the array
            int arraylength = artarray.length();
            for (int i = 0; i < arraylength; i++){
                // get the separate objects from the array
                JSONObject currentArtwork = artarray.getJSONObject(i);

                // find the artistname in the object
                String maker = currentArtwork.getString("principalOrFirstMaker");
                // save the names in the stringbuilder
                names.append(maker + "\n");

                // find the objectnumber in the object
                String objectnumber = currentArtwork.getString("id");
                // save the objectnumber in the stringbuilder
                ids.append(objectnumber + "\n");

            }

        } catch (JSONException e){
            // Log the type of error that got generated
            Log.e("JSON", e.getMessage());
        }
    }

    public void parseRMImage(JSONObject response){

        // parses a Rijkmuseum imagetiles-endpoint request
        // check if there actually is a response
        if(response == null || response.length() == 0){
            // there was no response
            Log.e("JSON", "There was no response from the JSONrequest or the request was " +
                    "empty.");
            return;
        }

        try {
            // find the array that contains the levels
            JSONArray levelsarray = response.getJSONArray("levels");

            // loop through the array of levels
            int arraylength = levelsarray.length();
            for (int i = 0; i < arraylength; i++){

                // get the separate levels from the array
                JSONObject currentLevel = levelsarray.getJSONObject(i);

                // we only want the imageurl from one tile
                if (Objects.equals(currentLevel.getString("name"), "z0")) {

                    // we have the smallest level
                    // find the tiles in the object
                    JSONArray tiles = currentLevel.getJSONArray("tiles");
                    Log.e("JSONobject", "this is the format of JSONObject tiles: " + tiles.toString());

                    String url = tiles.getJSONObject(0).getString("url");
                    Log.e("TILEURL", url);
                    result = url;
                    // we dont need to continu the for-loop, we have found what we wants
                    return;
                }
            }
        } catch (JSONException e){
            // Log the type of error that got generated
            Log.e("JSON", e.getMessage());
        }
    }

    public String getRMimageURL(){

        return result;
    }


    public String[] getRMartistnames(){
        // retrieve the artistnames from the Rijksmuseum

        if (this.names == null){
            // the Stringbuilder was empty
            Log.e("PARSER", "Please first parse the response from the Rijksmuseum API using " +
                    "parseRMcollection() method");
            String[] failure = {"error"};
            return failure;
        }
        else{
            // convert the stringbuilder to stringarray
            String stringofnames = this.names.toString();
            String[] result = stringofnames.split(Pattern.quote("\n"));

            // remove any commas at the end of the names (the Rijksmuseum puts these there, but they
            // do not look nice in the UI)
            int resultlenght = result.length;
            for(int i = 0; i < resultlenght; i++){
                result[i] = result[i].trim();
                if (result[i].endsWith(",")){
                    result[i] = result[i].substring(0, result[i].length() - 1);
                }
            }
            // return the result
            return result;
        }
    }


    public String[] getRMobjectids() {

        if (this.ids == null){
            // the Stringbuilder was empty
            Log.e("PARSER", "Please first parse the response from the Rijksmuseum API using " +
                    "parseRMcollection() method");
            String[] failure = {"error"};
            return failure;
        }
        else{
            // convert the stringbuilder to stringarray
            String stringofids = this.ids.toString();
            String[] result = stringofids.split(Pattern.quote("\n"));

            // return the result
            return result;
        }
    }
}
