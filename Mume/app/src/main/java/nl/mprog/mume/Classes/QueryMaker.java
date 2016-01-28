/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* QueryMaker is an object used to compose queries that can be used to request data with
   Volley in http GET methods */

package nl.mprog.mume.Classes;

import android.util.Log;

public class QueryMaker {

    private String searchtype;
    private String searchwords;
    private String requesturl;
    private String urlbase;

    // search parameters
    private String apikey = "key=e0SrHwkM";
    private String dataformat = "format=json";
    private String imageonly = "imgonly=True";

    // search parameter to get the pages of searchresults
    // standard value should be the first one (1 is the first page)
    private String page = "p=1";


    public QueryMaker(){

        //constructor

    }


    // !!! If the searchtype is "object" or "image", the searchword needs to be an objectnumber !!!
    public String getRequestURL(String searchwords){

        // get the searchwords and format them correctly
        this.searchwords = searchwords;
        formatSearchwords();

        if (searchtype == "collection"){
            // Build up the request url to retrieve json from the collection endpoint
            urlbase = "https://www.rijksmuseum.nl/api/en/collection?q=";
            this.requesturl = urlbase + this.searchwords + "&" + this.imageonly + "&s=relevance"
                    + "&" + this.apikey + "&" + this.dataformat + "&" + getPage();
            return this.requesturl;
        }
        else if ((searchtype == "image") && (this.searchwords.length() > 3)){
            // Build up the request url to retrieve json from the collection-image endpoint
            urlbase = "https://www.rijksmuseum.nl/api/en/collection/";
            // for the request to work we need to remove the "en-" prefix from the objectnumber:
            // we will remove the first three characters, by getting the substring started
            // at char at position 3:
            this.searchwords = searchwords.substring(3);
            this.requesturl = urlbase + this.searchwords + "/tiles?" + this.apikey + "&"
                    + this.dataformat;
            return this.requesturl;
        }
        else if ((searchtype == "object") && (this.searchwords.length() > 3)){
            // Build up the request url to retrieve json from collection details endpoint
            urlbase = "https://www.rijksmuseum.nl/api/en/collection/";
            this.searchwords = searchwords.substring(3);
            this.requesturl = urlbase + this.searchwords + "?" + this.apikey + "&"
                    + this.dataformat;
            return this.requesturl;
        }
        else {
            // this should never happen!
            Log.e("INVALID SEARCHTYPE", "The searchtype provided to the QueryMaker " +
                    "Class was invalid. Please either use \"collection\", \"object\" or \"image\"");
            return "failure";
        }
    }


    private void formatSearchwords(){
        // we cannot have spaces or whitespace in our url
        // remove any trailing whitespace
        this.searchwords = this.searchwords.trim();
        // replace any spaces in between the searchwords with "+"
        this.searchwords = this.searchwords.replaceAll(" ", "+");
    }


    public void setSearchtype(String searchtype) {
        this.searchtype = searchtype;
    }


    public String getPage() {
        return page;
    }


    public void setPage(String page) {
        this.page = page;
    }
}
