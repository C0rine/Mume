/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Resource: https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156
   resource for header and footer: http://stackoverflow.com/questions/26530685 */

package nl.mprog.mume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import nl.mprog.mume.Activities.ResultsActivity;
import nl.mprog.mume.Classes.VolleySingleton;
import nl.mprog.mume.R;


public class FacebookImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private String[] urls;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public FacebookImagesAdapter(Context context, String[] urls){
        // constructor
        this.urls = urls;
        this.context = context;

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getmImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.cardview_layout, parent, false);
            return new PhotoViewHolder(itemView);
        }
        else if (viewType == TYPE_HEADER){
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.search_header, parent, false);
            return new VHHeader(itemView);
        }

        throw new RuntimeException("there is not type that matches the type " + viewType
                + " make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PhotoViewHolder){
            // fill the cards with the data from Facebook
            PhotoViewHolder PVholder = (PhotoViewHolder) holder;
            String urlItem = getItem(position);
            PVholder.imageView.setImageUrl(urlItem, imageLoader);
            PVholder.textView.setText(urlItem);
        }
        else if (holder instanceof VHHeader){
            final VHHeader VHholder = (VHHeader) holder;
            Log.e("HOLDER", "there is an instance of VHHeader");
            // set the stuff in the header

            // handle a press on the search-icon in the searchbar edittext
            // resource: http://stackoverflow.com/questions/3554377
            VHholder.searchbar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (VHholder.searchbar.getRight() - VHholder.searchbar.getCompoundDrawables()
                                [2].getBounds().width())) {

                            // open new activity to show the results of the search
                            Intent startSearch = new Intent(context, ResultsActivity.class);
                            startSearch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // send the searchwords along to the next activity
                            startSearch.putExtra("searchwords", VHholder.searchbar.getText().toString());
                            context.startActivity(startSearch);

                            // empty the edittext (just show a hint)
                            VHholder.searchbar.setText("");

                            return true;
                        }
                    }
                    return false;
                }
            });

            // handle a press on the 'Go' button in the on screen keyboard
            // resource: http://developer.android.com/training/keyboard-input/style.html
            VHholder.searchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        // open new activity to show the results of the search
                        Intent startSearch = new Intent(context, ResultsActivity.class);
                        startSearch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // send the searchwords along to the next activity
                        startSearch.putExtra("searchwords", VHholder.searchbar.getText().toString());
                        context.startActivity(startSearch);

                        // empty the edittext (just show a hint)
                        VHholder.searchbar.setText("");
                        handled = true;
                    }
                    return handled;
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return urls.length + 1;
    }

    @Override
    public int getItemViewType(int position){
        if (isPositionHeader(position)){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }

    private String getItem(int position){

        return urls[position-1];
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView imageView;
        public TextView textView;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            imageView = (NetworkImageView) itemView.findViewById(R.id.album_imageview);
            textView = (TextView) itemView.findViewById(R.id.album_textview);

        }
    }

    public static class VHHeader extends RecyclerView.ViewHolder {

        public EditText searchbar;
        public Button startButton;

        public VHHeader(View itemView){
            super(itemView);

            searchbar = (EditText) itemView.findViewById(R.id.searchbar_edittext);
            startButton = (Button) itemView.findViewById(R.id.startsearch_button);
        }
    }

}


