/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Volley Singleton: because Volley is designed to queue all my requests. There should only
   be one queue, hence the singleton.
   Resources: http://stackoverflow.com/questions/19628795/
              https://www.youtube.com/watch?v=ohkPZw-gY3g&list=PLonJJ3BVjZW6CtAMbJz1XD8ELUs1KXaTD&index=29
              http://developer.android.com/training/volley/simple.html */

package nl.mprog.mume.Classes;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton sInstance=null;

    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;


    private VolleySingleton(){

        //constructor
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            // Allocate cache space for the image(s) we want to retrieve
            private LruCache<String, Bitmap> cache = new LruCache<>((int) (Runtime.getRuntime().maxMemory()/1024)/8);


            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }


    public static VolleySingleton getInstance(){

        if (sInstance == null){
            sInstance = new VolleySingleton();
        }

        return sInstance;

    }


    public RequestQueue getmRequestQueue(){
        return mRequestQueue;
    }


    public ImageLoader getmImageLoader(){
        return mImageLoader;
    }

}
