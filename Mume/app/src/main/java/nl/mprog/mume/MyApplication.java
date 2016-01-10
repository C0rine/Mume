/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/*  Used to get the context for making the Volley RequestQueue in VolleySingleton.java
    Resources: https://www.youtube.com/watch?v=T4SF7S6pYfE&list=PLonJJ3BVjZW6CtAMbJz1XD8ELUs1KXaTD&index=31*/

package nl.mprog.mume;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

}
