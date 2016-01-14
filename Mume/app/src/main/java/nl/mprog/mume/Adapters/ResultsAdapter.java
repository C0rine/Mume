/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Fits searchresults into a gridview
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

    public ResultsAdapter(Context c, String[] artistnames, String[] objectids) {
        this.context = c;
        this.artistnames = artistnames;
        this.objectids = objectids;

        imageRetriever = new ImageRetriever();
    }

    public int getCount() {
        // how many items do we want in the gridview?:
        return objectids.length;
    }

    public String getItem(int position) {
        return objectids[position];
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
        //theTextview.setText(textviewtext);

        // 2) an imageview to hold the thumbnail
        final ImageView theImageView = (ImageView) convertView.findViewById(R.id.thumbnail_imageview);
        theImageView.setImageResource(R.mipmap.art_nightwatch_square);
        theImageView.setAdjustViewBounds(true);

        // 3) retrieve the thumbnail imageRetriever url for the imageview
        // create the request queue using Volley
        RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

        // Create query
        QueryMaker queryMaker = new QueryMaker();
        queryMaker.setSearchtype("image");

        // For each objectid retrieve an image url for the thumbnail
        // resource: http://stackoverflow.com/questions/28120029
        imageRetriever.retrieveURL(requestQueue, queryMaker, objectids[position], new ImageRetriever.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                theTextview.setText(result);
            }
        });

        return convertView;
    }
}
