/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Fits searchresults into a gridview
   Resource: http://developer.android.com/guide/topics/ui/layout/gridview.html */

package nl.mprog.mume;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;

    public ResultsAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 10;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ){
            //We must create a View:
            inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.gridview_layout, parent, false);
        }

        // Placeholder text
        String artist = "Rembrandt";

        TextView theTextview = (TextView) convertView.findViewById(R.id.textview);
        theTextview.setText(artist);

        ImageView theImageView = (ImageView) convertView.findViewById(R.id.imageview);
        theImageView.setImageResource(R.mipmap.art_nightwatch_square);
        theImageView.setAdjustViewBounds(true);

        return convertView;
    }
}
