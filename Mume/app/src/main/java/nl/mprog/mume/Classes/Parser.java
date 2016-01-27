/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Parses the data retrieved by Volley and QueryMaker
   Resources: https://www.youtube.com/watch?v=5GzVtP0IODU */

package nl.mprog.mume.Classes;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class Parser {

    // Stringbuilders for parsing collection
    private StringBuilder names = new StringBuilder();
    private StringBuilder ids = new StringBuilder();
    private StringBuilder urlBigImages = new StringBuilder();

    // Stringbuilders for parsing collection detail
    private StringBuilder principalMakers = new StringBuilder();
    private StringBuilder titles = new StringBuilder();
    private StringBuilder datings = new StringBuilder();
    private StringBuilder materials = new StringBuilder();

    private JSONArray levelsarray;
    private String imageurl;


    public Parser(){
        //constructor
    }

    // parses a Rijksmuseum collection-endpoint request
    public void parseRMCollection(JSONObject response){

        // check if there actually is a response
        if(response == null || response.length() == 0){
            // there was no response
            Log.e("PARSER", "There was no response from the parseRMCOllection request or the " +
                    "request was empty.");
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
                // save the name in the stringbuilder
                if (maker != null && !maker.equals("")){
                    names.append(maker + "\n");
                }
                else {
                    names.append(" \n");
                }

                // find the objectnumber in the object
                String objectnumber = currentArtwork.getString("id");
                // save the objectnumber in the stringbuilder
                if (objectnumber != null && !objectnumber.equals("")){
                    ids.append(objectnumber + "\n");
                }
                else {
                    ids.append(" \n");
                }

                // find the url to the bigger image of the artwork
                String url = currentArtwork.getJSONObject("webImage").getString("url");
                if (url != null && !url.equals("")){
                    urlBigImages.append(url + "\n");
                }
                else{
                    urlBigImages.append(" \n");
                }
            }

        } catch (JSONException e){
            // Handle any possible errors
            Log.e("PARSER", "There was an error in parsing for RM Collection endpoint: "
                    + e.getMessage());
        }
    }


    // parses a Rijkmuseum imagetiles-endpoint request
    public void parseRMImage(JSONObject response){

        // check if there actually is a response
        if(response == null || response.length() == 0){
            // there was no response
            Log.e("PARSER", "There was no response from the parseRMImage request or the request " +
                    "was empty.");
            return;
        }

        try {
            // find the array that contains the levels
            levelsarray = response.getJSONArray("levels");

            // loop through the array of levels
            int arraylength = levelsarray.length();
            for (int i = 0; i < arraylength; i++){

                // get the separate levels from the array
                JSONObject currentLevel = levelsarray.getJSONObject(i);

                // we only want the imageurl from one tile
                if (Objects.equals(currentLevel.getString("name"), "z0")) {

                    // we have the level with the largest image
                    // find the tiles in this level
                    JSONArray tiles = currentLevel.getJSONArray("tiles");
                    // get the url from the first tile
                    String url = tiles.getJSONObject(0).getString("url");
                    imageurl = url;

                    // we dont need to continue the for-loop, we have found what we wants so:
                    return;
                }
            }
        } catch (JSONException e){
            // Handle any possible errors
            Log.e("PARSER", "There was an error in parsing for RM collection-image endpoint: "
                    + e.getMessage());
        }
    }


    public void parseRMcollectiondetail(JSONObject response){

        // parses a Rijkmuseum collection-detail endpoint request
        // check if there actually is a response
        if(response == null || response.length() == 0){
            // there was no response
            Log.e("PARSER", "There was no response from the parseRMcollectiondetail request or " +
                    "the request was empty.");
            return;
        }

        try {
            // find the object that contains the metadata on the artobject
            JSONObject theobject = response.getJSONObject("artObject");

            // get and safe the names
            JSONArray namesarray = theobject.getJSONArray("makers");
            int length = namesarray.length();
            for (int i = 0; i < length; i ++){
                principalMakers.append(namesarray.getJSONObject(i).getString("name") + "\n");
            }

            // get and safe the title
            titles.append(theobject.getString("title")  + "\n");

            // get and safe the dating
            if (theobject.getJSONObject("dating").getString("year") == null){
                datings.append("unknown"  + "\n");
            }
            else{
                datings.append(Integer.toString(theobject.getJSONObject("dating").getInt("year")));
            }

            // get and safe the materials
            JSONArray materialsarray = theobject.getJSONArray("materials");
            int length2 = materialsarray.length();
            for (int i = 0; i < length2; i ++){
                materials.append(materialsarray.getString(i)  + "\n");
            }


        } catch (JSONException e){
            // Handle any possible errors
            Log.e("PARSER", "There was an error in parsing for RM collection-detail endpoint: "
                    + e.getMessage());
        }
    }


    public JSONArray getRMImageLevelsarray() {
        return levelsarray;
    }


    public String[] getRMbigImageUrl(){

        String stringofurls = this.urlBigImages.toString();
        String[] result = stringofurls.split(Pattern.quote("\n"));

        return result;

    }


    public String[] getRMartistnames(){

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


    public String getRMprincipalmakers() {
        // retrieve the objectids from the parsed Rijksmuseum collection-endpoint request.

        if (this.ids == null){
            // the Stringbuilder was empty
            Log.e("PARSER", "Please first parse the response from the Rijksmuseum API using " +
                    "parseRMcollection() method");
            return "N/A";
        }
        else{
            // convert the stringbuilder to stringarray
            String stringofnames = this.principalMakers.toString();
            // return the result
            return stringofnames;
        }
    }


    public String getRMtitle() {
        // retrieve the objectids from the parsed Rijksmuseum collection-endpoint request.

        if (this.ids == null){
            // the Stringbuilder was empty
            Log.e("PARSER", "Please first parse the response from the Rijksmuseum API using " +
                    "parseRMcollection() method");
            return "N/A";
        }
        else{
            // convert the stringbuilder to stringarray
            String stringoftitles = this.titles.toString();
            // return the result
            return stringoftitles;
        }
    }


    public String getRMdating() {

        if (this.ids == null){
            // the Stringbuilder was empty
            Log.e("PARSER", "Please first parse the response from the Rijksmuseum API using " +
                    "parseRMcollection() method");
            return "N/A";
        }
        else{
            // convert the stringbuilder to stringarray
            String stringofdatings = this.datings.toString();
            // return the result
            return stringofdatings;
        }
    }


    public String getRMmaterials() {

        if (this.ids == null){
            // the Stringbuilder was empty
            Log.e("PARSER", "Please first parse the response from the Rijksmuseum API using " +
                    "parseRMcollection() method");
            return "N/A";
        }
        else{
            // convert the stringbuilder to stringarray
            String stringofmaterials = this.materials.toString();

            // return the result
            return stringofmaterials;
        }
    }
}
