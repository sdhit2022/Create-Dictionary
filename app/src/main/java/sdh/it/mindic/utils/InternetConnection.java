package sdh.it.mindic.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetConnection {
    public static boolean checkConnection(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null){
          //  Toast.makeText(context, networkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
            if (networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                Log.d("INTERNET_CONNECTION", "TYPE_WIFI");
                return true;
            }else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                Log.d("INTERNET_CONNECTION", "TYPE_MOBILE");
                return true;
            }
        }
        Log.d("INTERNET_CONNECTION", "NO_CONNECTION");
        return false;
    }
}
