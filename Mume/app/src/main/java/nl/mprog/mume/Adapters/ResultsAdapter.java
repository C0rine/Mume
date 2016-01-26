/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Fits searchresults of Rijksmuseum API into a gridview.
   Resource: http://developer.android.com/guide/topics/ui/layout/gridview.html */

package nl.mprog.mume.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;

import nl.mprog.mume.Classes.ImageRetriever;
import nl.mprog.mume.Classes.QueryMaker;
import nl.mprog.mume.Classes.VolleySingleton;
import nl.mprog.mume.R;

public class ResultsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    private String[] artistnames;
    private String[] objectids;
    private String[] bigImageUrls;

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private ImageRetriever imageRetriever;

    public ResultsAdapter(Context c, String[] artistnames, String[] objectids, String[] bigImageUrls) {

        // constructor
        this.context = c;
        this.artistnames = artistnames;
        this.objectids = objectids;
        this.bigImageUrls = bigImageUrls;

        // Volley is used to make and manage network requests
        volleySingleton = VolleySingleton.getInstance();
        // imageloader is used to load images from a url
        imageLoader = volleySingleton.getmImageLoader();
        imageRetriever = new ImageRetriever();
    }


    // how many items do we want in the gridview?:
    public int getCount() {
        // one for each object
        return objectids.length;
    }


    // get the url for the big image
    public String getImage(int position){
        return bigImageUrls[position];
    }


    // get the url to get detailed info on an item (artwork)
    public String getItem(int position) {
        // create a query url
        String searchword = objectids[position];

        QueryMaker queryMaker = new QueryMaker();
        String searchtype = "object";
        queryMaker.setSearchtype(searchtype);

        String url = queryMaker.getRequestURL(searchword);
        return url;
    }

    // compulsory method for a BaseAdapter (but I don't use this)
    public long getItemId(int position) {
        return 0;
    }


    // creates views for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            //There is no view so we must create a new View. We will inflate it from a custom layout
            this.inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.gridview_layout, parent, false);
        }

        // For each item create:
        // 1) a textview to hold the artistname
        final TextView thumbnailtitle = (TextView) convertView.findViewById(R.id.thumbnailtitle_textview);
        String textviewtext = artistnames[position];
        thumbnailtitle.setText(textviewtext);

        // 2) an imageview to hold the thumbnail (which we will temporarily fill with filler image)
        final ImageView thumbnailimage = (ImageView) convertView.findViewById(R.id.thumbnail_imageview);
        thumbnailimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        thumbnailimage.setImageResource(R.mipmap.image_viewfiller);

        // 3) retrieve the thumbnail url for the imageview and set the actual thumbnail
        // create the request queue using Volley
        RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

        // Create query
        QueryMaker queryMaker = new QueryMaker();
        queryMaker.setSearchtype("image");

        // retrieve the image
        // resource: http://stackoverflow.com/questions/28120029
        imageRetriever.retrieveRMThumbnailArray(requestQueue, queryMaker, objectids[position], new ImageRetriever.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                JSONArray levels = result;

                // get the url for the image
                String URL = imageRetriever.getThumbnailURL(levels);

                if (URL != null) {
                    imageLoader.get(URL, new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Failed to get the image. Set a standard image in the view
                            thumbnailimage.setImageResource(R.mipmap.image_notavailable_icon);
                            Log.e("RESULTSADAPTER", "There was an error in loading the thumbnail image: "
                                    + error.getMessage());
                        }

                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            // Success! there was an image url retrieved. Set this image in the imageview.
                            thumbnailimage.setAlpha(0f);
                            thumbnailimage.setImageBitmap(response.getBitmap());
                            thumbnailimage.animate().alpha(1f).setDuration(1000);
                        }
                    });
                } else {
                    // the url was not there, set a standard image in the view.
                    thumbnailimage.setImageResource(R.mipmap.image_notavailable_icon);
                }
            }
        });

        return convertView;
    }
}
