/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* This adapter is used to display the facebook images in the search-activity. It contains a body with
   the images, a header with the searchbar and some textviews and a footer with a button to the Classical Art Memes
   Facebook page.
   Resource: https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156
   resource for header and footer: http://stackoverflow.com/questions/26530685 */

package nl.mprog.mume.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import nl.mprog.mume.Activities.ResultsActivity;
import nl.mprog.mume.Classes.MyApplication;
import nl.mprog.mume.Classes.VolleySingleton;
import nl.mprog.mume.R;


public class FacebookImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private String[] urls;
    private String[] posturls;
    private String[] dates;
    private String[] names;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BODYITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public FacebookImagesAdapter(Context context, String[] urls, String[] posturls, String[] dates, String[] names){
        // constructor
        this.context = context;
        this.urls = urls;
        this.posturls = posturls;
        this.dates = dates;
        this.names = names;

        // Volley is used to make network requests
        volleySingleton = VolleySingleton.getInstance();
        // the imageloader is used to load images from a url
        imageLoader = volleySingleton.getmImageLoader();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the right layout for the right viewtype
        if (viewType == TYPE_BODYITEM){
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
        else if (viewType == TYPE_FOOTER){
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.search_footer, parent, false);
            return new VHFooter(itemView);
        }

        throw new RuntimeException("there is not type that matches the type " + viewType
                + " make sure your using types correctly");
    }


    // set the content of the header, body and footer.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Set the content of the body:
        if (holder instanceof PhotoViewHolder){
            // fill the cards with the data from Facebook
            final PhotoViewHolder bodyitem = (PhotoViewHolder) holder;

            // set the right caption/name and date/time:
            String dateItem = getDate(position);
            String nameItem = getName(position);
            bodyitem.date.setText(dateItem);
            bodyitem.caption.setText(nameItem);

            // use the image loader to get and set the image
            String urlItem = getUrl(position);
            imageLoader.get(urlItem, new ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Failure: set a standard image if this image could not load
                    bodyitem.memeimage.setImageResource(R.mipmap.image_notavailable_icon);
                    Log.e("FACEBOOK IMAGES ADAPTER", "There was an error in loading the Facebook image: "
                            + error.getMessage());
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    // Success! there was an image url retrieved. Set the image in the imageview
                    // use animation to slowly let the image fade in
                    bodyitem.memeimage.setAlpha(0f);
                    bodyitem.memeimage.setImageBitmap(response.getBitmap());
                    bodyitem.memeimage.animate().alpha(1f).setDuration(800);
                }
            });

            // set the behaviour for the button to the post on Facebook
            final String pageurl = getPostUrl(position);
            bodyitem.showFBpost.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // open the post in the Facebook app if it is present. If not, open in browser.
                    try {
                        // open in FB app
                        // resource: http://stackoverflow.com/questions/4810803/open-facebook-page-from-android-app
                        context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        String id = pageurl.substring(20);
                        Intent openFacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/classicalartmemes/photos/a.595162167262642.1073741827.595155763929949/" + id));
                        openFacebookIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(openFacebookIntent);
                    } catch (Exception e) {
                        // open in browser
                        Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageurl));
                        openBrowserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(openBrowserIntent);
                    }
                }
            });
        }
        // set the content of the header:
        else if (holder instanceof VHHeader){
            final VHHeader header = (VHHeader) holder;

            // handle a press on the search-icon in the searchbar edittext
            // resource: http://stackoverflow.com/questions/3554377
            header.searchbar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (header.searchbar.getRight() - header.searchbar.getCompoundDrawables()
                                [2].getBounds().width())) {
                            // open new activity to show the results of the search
                            Intent startSearch = new Intent(context, ResultsActivity.class);
                            startSearch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // send the searchwords along to the next activity
                            startSearch.putExtra("searchwords", header.searchbar.getText().toString());
                            context.startActivity(startSearch);
                            // empty the edittext (just show a hint)
                            header.searchbar.setText("");
                            return true;
                        }
                    }
                    return false;
                }
            });

            // handle a press on the 'Go' button in the on screen keyboard
            // resource: http://developer.android.com/training/keyboard-input/style.html
            header.searchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        // open new activity to show the results of the search
                        Intent startSearch = new Intent(context, ResultsActivity.class);
                        startSearch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // send the searchwords along to the next activity
                        startSearch.putExtra("searchwords", header.searchbar.getText().toString());
                        context.startActivity(startSearch);

                        // empty the edittext (just show a hint)
                        header.searchbar.setText("");
                        handled = true;
                    }
                    return handled;
                }
            });

            // if there a no items (besides the header and footer) show the user that he/she needs to login to view the items (photos)
            if (getItemCount() < 3){
                header.cardviewheader.setText(R.string.pleaseloginfb_textview_text);
            }
        }
        // set content for footer:
        else if (holder instanceof VHFooter){
            final VHFooter footer = (VHFooter) holder;

            // set onclick listener to the button to the FB page
            footer.showFBpage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // open the page in the Facebook app if it is present. If not, open in browser.
                    try {
                        context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        // resource: http://stackoverflow.com/questions/4810803/open-facebook-page-from-android-app
                        Intent openFacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/classicalartmemes"));
                                openFacebookIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(openFacebookIntent);
                    } catch (Exception e) {
                        Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/classicalartmemes"));
                        openBrowserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(openBrowserIntent);
                    }
                }
            });

            // if there a no items (besides the header and footer), hide the button to the FB page
            if (getItemCount() < 3){
                footer.showFBpage.setVisibility(View.GONE);
            }
        }
    }

    // how many items do we want in our recyclerview
    @Override
    public int getItemCount() {
        // +2 fot the footer and header
        return urls.length + 2;
    }

    // check the viewtype of a position
    @Override
    public int getItemViewType(int position){
        if (isPositionHeader(position)){
            return TYPE_HEADER;
        }
        else if (isPositionFooter(position)){
            return TYPE_FOOTER;
        }
        return TYPE_BODYITEM;
    }

    // the first item (position 0) is the header
    private boolean isPositionHeader(int position){
        return position == 0;
    }

    // the last item is the footer
    private boolean isPositionFooter(int position){
        return position == getItemCount() - 1;
    }

    // -1 because of the header (which has the first position)
    private String getUrl(int position){
        return urls[position-1];
    }

    // -1 because of the header (which has the first position)
    private String getPostUrl(int position){
        return posturls[position-1];
    }

    // -1 because of the header (which has the first position)
    private String getDate(int position){
        return dates[position-1];
    }

    // -1 because of the header (which has the first position)
    private String getName(int position){
        return names[position-1];
    }


    // initialize the view for body items
    public static class PhotoViewHolder extends RecyclerView.ViewHolder{

        public ImageView memeimage;
        public TextView date;
        public TextView caption;
        public Button showFBpost;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            memeimage= (ImageView) itemView.findViewById(R.id.album_imageview);
            date = (TextView) itemView.findViewById(R.id.album_textview);
            caption = (TextView) itemView.findViewById(R.id.album_textview2);
            showFBpost = (Button) itemView.findViewById(R.id.buttonToFB);
        }
    }


    // initialize the views for the header
    public static class VHHeader extends RecyclerView.ViewHolder {

        public EditText searchbar;
        public TextView cardviewheader;

        public VHHeader(View itemView){
            super(itemView);
            searchbar = (EditText) itemView.findViewById(R.id.searchbar_edittext);
            cardviewheader = (TextView) itemView.findViewById(R.id.cardview_title);
        }
    }


    // initialize the views for the footer
    public static class VHFooter extends RecyclerView.ViewHolder {

        public Button showFBpage;

        public VHFooter(View itemView){
            super(itemView);
            showFBpage = (Button) itemView.findViewById(R.id.FBPage_button);
        }
    }
}


