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

public class Parser {

    private StringBuilder names;

    public Parser(){

        //constructor

    }


    public void parseRMCollection(JSONObject response){

        // parses a Rijkmusuem collection-endpoint request
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

            // create a stringbuilder to hold the artistnames
            this.names = new StringBuilder();

            // loop through the array
            int arraylength = artarray.length();
            for (int i = 0; i < arraylength; i++){

                // get the separate objects from the array
                JSONObject currentArtwork = artarray.getJSONObject(i);
                String maker = currentArtwork.getString("principalOrFirstMaker");
                // save them in the stringbuilder
                this.names.append(maker);

            }

        } catch (JSONException e){
            Log.e("JSON", "The array you are trying to find does not exist in the JSONresponse.");
        }
    }


    public String getRMartistnames(){
        // retrieve the artistnames from the Rijksmuseum

        if (this.names == null){
            // the Stringbuilder was empty
            Log.e("PARSER", "Please first parse the response from the Rijksmuseum API using " +
                    "parseRMcollection() method");
            return "failure";
        }
        else{
            // convert the stringbuilder to string and return the string
            String result = this.names.toString();
            return result;
        }
    }


}
