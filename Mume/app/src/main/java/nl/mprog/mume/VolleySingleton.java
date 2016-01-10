/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Volley Singleton: because Volley is designed to queue all my requests. There should only
   be one queue, hence the singleton.
   Resources: http://stackoverflow.com/questions/19628795/
              https://www.youtube.com/watch?v=ohkPZw-gY3g&list=PLonJJ3BVjZW6CtAMbJz1XD8ELUs1KXaTD&index=29
              http://developer.android.com/training/volley/simple.html */

package nl.mprog.mume;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton sInstance=null;

    private RequestQueue mRequestQueue;

    private VolleySingleton(){

        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

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

}
