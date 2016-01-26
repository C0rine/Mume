/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Resource: https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156
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
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.login.widget.LoginButton;

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
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public FacebookImagesAdapter(Context context, String[] urls, String[] posturls, String[] dates, String[] names){
        // constructor
        this.context = context;
        this.urls = urls;
        this.posturls = posturls;
        this.dates = dates;
        this.names = names;

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
        else if (viewType == TYPE_FOOTER){
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.search_footer, parent, false);
            return new VHFooter(itemView);
        }

        throw new RuntimeException("there is not type that matches the type " + viewType
                + " make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PhotoViewHolder){
            // fill the cards with the data from Facebook
            final PhotoViewHolder PVholder = (PhotoViewHolder) holder;
            String urlItem = getUrl(position);
            String dateItem = getDate(position);
            String nameItem = getName(position);
//            PVholder.imageView.setImageUrl(urlItem, imageLoader);
            PVholder.textView.setText(dateItem);
            PVholder.textView2.setText(nameItem);

            // use the image loader to get the image
            imageLoader.get(urlItem, new ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Failure: set a standard image if this image could not load
                    PVholder.imageView.setImageResource(R.mipmap.image_notavailable_icon);
                    Log.e("FACEBOOK IMAGES ADAPTER", "There was an error in loading the Facebook image: "
                            + error.getMessage());
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    // Success! there was an image url retrieved. Set the image in the imageview
                    PVholder.imageView.setAlpha(0f);
                    PVholder.imageView.setImageBitmap(response.getBitmap());
                    PVholder.imageView.animate().alpha(1f).setDuration(800);
                }
            });

            final String pageurl = getPostUrl(position);
            // set the behaviour for the button
            PVholder.button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // open the post in the Facebook app if it is present. If not, open in browser.
                    try {
                        context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                        String id = pageurl.substring(20);
                        // resource: http://stackoverflow.com/questions/4810803/open-facebook-page-from-android-app
                        Intent openFacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/classicalartmemes/photos/a.595162167262642.1073741827.595155763929949/" + id));
                        openFacebookIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(openFacebookIntent);
                    } catch (Exception e) {
                        Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageurl));
                        openBrowserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(openBrowserIntent);
                    }
                }
            });

        }
        else if (holder instanceof VHHeader){
            final VHHeader VHholder = (VHHeader) holder;
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

            if (getItemCount() < 3){
                VHholder.cardview_header.setText("Login with Facebook to see the memes of the official Classical Art Memes Facebook page");
            }
        }
        else if (holder instanceof VHFooter){
            final VHFooter VHfooter = (VHFooter) holder;

            VHfooter.toFacebookPage.setOnClickListener(new View.OnClickListener() {
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

            if (getItemCount() < 3){
                VHfooter.toFacebookPage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return urls.length + 2;
    }

    @Override
    public int getItemViewType(int position){
        if (isPositionHeader(position)){
            return TYPE_HEADER;
        }
        else if (isPositionFooter(position)){
            Log.e("FOOTER", "we have found the footer");
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }

    private boolean isPositionFooter(int position){
        return position == getItemCount() - 1;
    }

    private String getUrl(int position){

        return urls[position-1];
    }

    private String getPostUrl(int position){

        return posturls[position-1];
    }

    private String getDate(int position){

        return dates[position-1];
    }

    private String getName(int position){

        return names[position-1];
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public TextView textView2;
        public Button button;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.album_imageview);
            textView = (TextView) itemView.findViewById(R.id.album_textview);
            textView2 = (TextView) itemView.findViewById(R.id.album_textview2);
            button = (Button) itemView.findViewById(R.id.buttonToFB);

        }

    }

    public static class VHHeader extends RecyclerView.ViewHolder {

        public EditText searchbar;
        public LoginButton loginButton;
        public TextView cardview_header;

        public VHHeader(View itemView){
            super(itemView);

            searchbar = (EditText) itemView.findViewById(R.id.searchbar_edittext);
            loginButton = (LoginButton) itemView.findViewById(R.id.login_button);
            cardview_header = (TextView) itemView.findViewById(R.id.cardview_title);
        }
    }

    public static class VHFooter extends RecyclerView.ViewHolder {

        public Button toFacebookPage;

        public VHFooter(View itemView){
            super(itemView);

            toFacebookPage = (Button) itemView.findViewById(R.id.FBPage_button);
        }
    }

}


