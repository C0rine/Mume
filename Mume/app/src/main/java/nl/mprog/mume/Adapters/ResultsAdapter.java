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

import java.util.Arrays;

import nl.mprog.mume.Classes.Image;
import nl.mprog.mume.Classes.Searcher;
import nl.mprog.mume.Classes.VolleySingleton;
import nl.mprog.mume.R;

public class ResultsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] artistnames;
    private String[] imageids;
    private String[] imageurls;
    private Image image;

    public ResultsAdapter(Context c, String[] artistnames, String[] imageids) {
        this.context = c;
        this.artistnames = artistnames;
        this.imageids = imageids;

        image = new Image();

        //imageurls = image.getImageURLS(this.imageids);
        //Log.e("URLS", Arrays.toString(image.getImageURLS(this.imageids)));
    }

    public int getCount() {
        // how many items do we want in the gridview?:
        return imageids.length;
    }

    public String getItem(int position) {
        return imageids[position];
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

        // 2) an imageview to hold the thumbnail
        final ImageView theImageView = (ImageView) convertView.findViewById(R.id.thumbnail_imageview);
        theImageView.setImageResource(R.mipmap.art_nightwatch_square);
        theImageView.setAdjustViewBounds(true);

        // 3) retrieve the image for the image
        // create the request queue using Volley
        RequestQueue requestQueue = VolleySingleton.getInstance().getmRequestQueue();

        // Create query
        Searcher searcher = new Searcher();
        searcher.setSearchtype("image");

        // Loop trough the imageids
        Log.e("COCO", imageids[position]);
        image.retrieveURL(requestQueue, searcher, imageids[position], new Image.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                theTextview.setText(result);
            }
        });

        return convertView;
    }
}
