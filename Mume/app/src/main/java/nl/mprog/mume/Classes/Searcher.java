/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Searcher is an object used to make a query. It takes one arguments: The type of search
   that needs to be made (either retrieving information from the collection or retrieving
   a specific record) */

package nl.mprog.mume.Classes;

import android.util.Log;

public class Searcher {

    private String searchtype;
    private String searchwords;
    private String requesturl;

    // search parameters
    private String apikey = "key=e0SrHwkM";
    private String dataformat = "format=json";
    private String imageonly = "imgonly=True";


    // searchtype has to be either "collection", "object" or "image"
    public Searcher(){

        //constructor

    }

    // If the searchtype is "object" or "image", the searchword needs to be an objectnumber
    public String getRequestURL(String searchwords){

        // get the searchwords and format them correctly
        this.searchwords = searchwords;
        formatSearchwords();

        String urlbase;
        if (searchtype == "collection"){
            // Build up the request url to retrieve json from the collection endpoint
            urlbase = "https://www.rijksmuseum.nl/api/en/collection?q=";
            this.requesturl = urlbase + this.searchwords + "&" + this.imageonly + "&" + this.apikey + "&" + this.dataformat;
            return this.requesturl;
        }
        else if (searchtype == "image"){
            urlbase = "https://www.rijksmuseum.nl/api/en/collection/";

            // for the request to work we need to remove the "en-" prefix from the objectnumber
            // we will remove the first three characters
            this.searchwords = searchwords.substring(3);

            this.requesturl = urlbase + this.searchwords + "/tiles?" + this.apikey + "&" + this.dataformat;

            Log.e("URL", "The request url is:  " + this.requesturl);

            return this.requesturl;
        }
        else if (searchtype == "object"){
            // Build up the request url to retrieve json from collection details endpoint
            urlbase = "https://www.rijksmuseum.nl/api/en/collection/";
            this.requesturl = urlbase + this.searchwords + "?" + this.apikey + "&" + this.dataformat;
            return this.requesturl;
        }
        else {
            // this should never happen!
            Log.e("INVALID SEARCHTYPE", "The searchtype provided to the Searcher Class was invalid. " +
                    "Please either use \"collection\", \"object\" or \"image\"");
            return "failure";
        }

    }


    private void formatSearchwords(){

        // remove any trailing whitespace
        this.searchwords = this.searchwords.trim();
        // replace any spaces in between the searchwords with "+"
        this.searchwords = this.searchwords.replaceAll(" ", "+");

    }


    public void setSearchtype(String searchtype) {
        this.searchtype = searchtype;
    }
}
