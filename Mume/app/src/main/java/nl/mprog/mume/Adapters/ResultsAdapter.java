/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Fits searchresults into a gridview
   Resource: http://developer.android.com/guide/topics/ui/layout/gridview.html */

package nl.mprog.mume.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import nl.mprog.mume.Classes.Image;
import nl.mprog.mume.R;

public class ResultsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] artistnames;
    private String[] imageids;
    private String[] imageurls;

    public ResultsAdapter(Context c, String[] artistnames, String[] imageids) {
        this.context = c;
        this.artistnames = artistnames;
        this.imageids = imageids;

        Image image = new Image();
        imageurls = image.getImageURLS(this.imageids);
    }

    public int getCount() {
        // how many items do we want in the gridview?:
        return artistnames.length;
    }

    public Object getItem(int position) {

        return null;
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
        TextView theTextview = (TextView) convertView.findViewById(R.id.thumbnailtitle_textview);
        String textviewtext = artistnames[position] + " "; //+ imageurls[position];
        theTextview.setText(textviewtext);

        // 2) an imageview to hold the thumbnail
        ImageView theImageView = (ImageView) convertView.findViewById(R.id.thumbnail_imageview);
        theImageView.setImageResource(R.mipmap.art_nightwatch_square);
        theImageView.setAdjustViewBounds(true);

        return convertView;
    }
}
