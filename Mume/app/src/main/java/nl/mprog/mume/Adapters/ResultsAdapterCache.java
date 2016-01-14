/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Fits searchresults into a gridview
   Resources: http://developer.android.com/guide/topics/ui/layout/gridview.html
              https://github.com/slidenerd/Material-Design-Test-App-DEPRECATED-/blob/master/app/src/main/java/materialtest/vivz/slidenerd/adapters/AdapterBoxOffice.java */

package nl.mprog.mume.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import nl.mprog.mume.Classes.ImageRetriever;
import nl.mprog.mume.Classes.VolleySingleton;
import nl.mprog.mume.R;

public class ResultsAdapterCache extends RecyclerView.Adapter<ResultsAdapterCache.ViewHolderResults> {

    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private String[] artistnames;
    private String[] objectids;
    private ImageLoader imageLoader;


    public ResultsAdapterCache(Context context) {

        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getmImageLoader();
    }

    public void setArtistNames(String[] artistnames) {
        this.artistnames = artistnames;
        notifyDataSetChanged();
    }

    public void setObjectIds(String[] objectids){
        this.objectids = objectids;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderResults onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.gridview_layout, parent, false);
        ViewHolderResults viewHolder = new ViewHolderResults(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderResults holder, int position) {

        String currentArtistName = this.artistnames[position];
        String currentObjectID = this.objectids[position];

        holder.thumbnailTitle.setText(currentArtistName);

        // we need the url of one image based on the objectnr.
        ImageRetriever imageRetriever = new ImageRetriever();
        imageRetriever.retrieveURL(currentObjectID, new ImageRetriever.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                String urlThumbnail = result;
                loadImages(urlThumbnail, holder);

            }
        });

    }


    private void loadImages(final String urlThumbnail, final ViewHolderResults holder) {
        if (urlThumbnail != null) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.thumbnailImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("RESULTSADAPTER", "The request to retrieve a thumbnail from url failed: "
                            + error.getMessage());
                    holder.thumbnailImage.setImageResource(R.mipmap.image_notavailable_icon);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return objectids.length;
    }

    static class ViewHolderResults extends RecyclerView.ViewHolder {

        ImageView thumbnailImage;
        TextView thumbnailTitle;

        public ViewHolderResults(View itemView) {
            super(itemView);
            thumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_imageview);
            thumbnailTitle = (TextView) itemView.findViewById(R.id.thumbnailtitle_textview);
        }
    }
}
