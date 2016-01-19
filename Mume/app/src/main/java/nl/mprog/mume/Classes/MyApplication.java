/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/*  Used to get the context for making the Volley RequestQueue in VolleySingleton.java
    Resources: https://www.youtube.com/watch?v=T4SF7S6pYfE&list=PLonJJ3BVjZW6CtAMbJz1XD8ELUs1KXaTD&index=31*/

/* Image editing with Adobe Creative SDK
   Resources: https://creativesdk.adobe.com/docs/android/#/articles/gettingstarted/index.html */

package nl.mprog.mume.Classes;

import android.app.Application;
import android.content.Context;

import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.internal.auth.AdobeAuthIMSEnvironment;
import com.aviary.android.feather.sdk.IAviaryClientCredentials;

public class MyApplication extends Application implements IAviaryClientCredentials {

    private static MyApplication sInstance;
    private static final String CREATIVE_SDK_CLIENT_ID = "675de7a926be4ce1903fe04a5544e788";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "b75ed7ae-a740-437e-9306-da788033a100";


    @Override
    public void onCreate(){
        super.onCreate();

        AdobeCSDKFoundation.initializeCSDKFoundation(
                getApplicationContext(),
                AdobeAuthIMSEnvironment.AdobeAuthIMSEnvironmentProductionUS
        );

        sInstance = this;
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    @Override
    public String getBillingKey() {
        return ""; // Leave this blank
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

}
