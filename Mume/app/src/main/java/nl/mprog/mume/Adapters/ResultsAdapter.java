/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Fits searchresults into a gridview
   Resource: http://developer.android.com/guide/topics/ui/layout/gridview.html */

package nl.mprog.mume.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.ArrayList;

import nl.mprog.mume.Classes.ImageRetriever;
import nl.mprog.mume.Classes.QueryMaker;
import nl.mprog.mume.Classes.VolleySingleton;
import nl.mprog.mume.R;

public class ResultsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] artistnames;
    private String[] objectids;
    private ImageRetriever imageRetriever;

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    final ArrayList<Bitmap> bitmapArrayList;

    public ResultsAdapter(Context c, String[] artistnames, String[] objectids) {

        // constructor
        this.context = c;
        this.artistnames = artistnames;
        this.objectids = objectids;

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getmImageLoader();
        bitmapArrayList = new ArrayList<Bitmap>();

        imageRetriever = new ImageRetriever();
    }

    public int getCount() {
        // how many items do we want in the gridview?:
        return objectids.length;
    }

    public String getItem(int position) {

        // create a query url
        String searchword = objectids[position];
        QueryMaker queryMaker = new QueryMaker();
        String searchtype = "object";
        queryMaker.setSearchtype(searchtype);
        String url = queryMaker.getRequestURL(searchword);

        return url;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // creates views for each item referenced by the Adapter

        if (convertView == null){
            //There is no view so we must create a new View. We will inflate it from a custom layout
            this.inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.gridview_layout, parent, false);
        }

        // For each item create:
        // 1) a textview to hold the artistname
        final TextView theTextview = (TextView) convertView.findViewById(R.id.thumbnailtitle_textview);
        String textviewtext = artistnames[position];
        theTextview.setText(textviewtext);

        // 2) an imageview to hold the thumbnail
        final ImageView theImageView = (ImageView) convertView.findViewById(R.id.thumbnail_imageview);
        theImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        theImageView.setImageResource(R.mipmap.image_viewfiller);

        // 3) retrieve the thumbnail imageRetriever url for the imageview
        // create the request queue using Volley
        RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

        // Create query
        QueryMaker queryMaker = new QueryMaker();
        queryMaker.setSearchtype("image");

        // resource: http://stackoverflow.com/questions/28120029
        imageRetriever.retrieveRMThumbnailArray(requestQueue, queryMaker, objectids[position], new ImageRetriever.VolleyCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                JSONArray levels = result;

                // get the url from the right level
                String URL = imageRetriever.getThumbnailURL(levels);

                if (URL != null) {

                    // use the image loader to get the image
                    imageLoader.get(URL, new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Failure: set a standard image if this image could not load
                            theImageView.setImageResource(R.mipmap.image_notavailable_icon);
                            Log.e("RESULTSADAPTER", "There was an error in loading the thumbnail image: "
                                    + error.getMessage());
                        }

                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            // Success! there was an image url retrieved. Set the image in the imageview
                            theImageView.setAlpha(0f);
                            theImageView.setImageBitmap(response.getBitmap());
                            theImageView.animate().alpha(1f).setDuration(1000);
                        }
                    });
                } else {
                    // the url was not there
                    theImageView.setImageResource(R.mipmap.image_notavailable_icon);
                }
            }
        });

        return convertView;
    }
}
