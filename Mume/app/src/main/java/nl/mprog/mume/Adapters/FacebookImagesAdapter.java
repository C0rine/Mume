/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Resource: https://www.binpress.com/tutorial/android-l-recyclerview-and-cardview-tutorial/156 */

package nl.mprog.mume.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import nl.mprog.mume.Classes.VolleySingleton;
import nl.mprog.mume.R;


public class FacebookImagesAdapter extends RecyclerView.Adapter<FacebookImagesAdapter.PhotoViewHolder>{

    private String[] urls;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public FacebookImagesAdapter(String[] urls){
        // constructor
        this.urls = urls;

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getmImageLoader();

    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.cardview_layout, parent, false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {

        holder.imageView.setImageUrl(urls[position], imageLoader);
        holder.textView.setText(urls[position]);

    }

    @Override
    public int getItemCount() {
        return urls.length;
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
}


