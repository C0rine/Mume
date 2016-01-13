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


    // searchtype has to be either "collection" or "object"
    public Searcher(String searchtype){

        //constructor
        this.searchtype = searchtype;

    }

    // If the searchtype is "object", the searchword needs to be an objectnumber
    public void createQuery(String searchwords){

        // get the searchwords and format them correctly
        this.searchwords = searchwords;
        formatSearchwords();

        String urlbase;
        if (searchtype == "collection"){
            // Build up the request url to retrieve json from the collection endpoint
            urlbase = "https://www.rijksmuseum.nl/api/en/collection?q=";
            this.requesturl = urlbase + this.searchwords + "&" + this.imageonly + "&" + this.apikey + "&" + this.dataformat;

        }
        else if (searchtype == "object"){
            // Build up the request url to retrieve json from collection details endpoint
            urlbase = "https://www.rijksmuseum.nl/api/en/collection/";
            this.requesturl = urlbase + this.searchwords + "?" + this.apikey + "&" + this.dataformat;
        }
        else {
            // this should never happen!
            Log.e("INVALID SEARCHTYPE", "The searchtype provided to the Searcher Class was invalid. " +
                    "Please either use \"collection\" or \"object\"");
        }

    }

    public String getRequesturl() {
        return requesturl;
    }

    private void formatSearchwords(){

        // remove any trailing whitespace
        this.searchwords = this.searchwords.trim();
        // replace any spaces in between the searchwords with "+"
        this.searchwords = this.searchwords.replaceAll(" ", "+");

    }

}
