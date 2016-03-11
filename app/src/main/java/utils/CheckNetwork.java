package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Josermando Peralta on 3/9/2016.
 */
public class CheckNetwork {
    private static final String LOG_TAG = CheckNetwork.class.getSimpleName();

    public static boolean isInternetAvailable(Context context){
        NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if(networkInfo == null){
            Log.d(LOG_TAG," No internet connection");
            return false;
        }
        else{
            if(networkInfo.isConnected()){
                Log.d(LOG_TAG," Internet connection available");
                return true;
            }
            else{
                Log.d(LOG_TAG, " Internet connection");
                return true;
            }
        }
    }
}
