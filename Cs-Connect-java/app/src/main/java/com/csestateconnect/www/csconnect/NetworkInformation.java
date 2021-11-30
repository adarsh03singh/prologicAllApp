package com.csestateconnect.www.csconnect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkInformation {

        private Context mContext;

        public NetworkInformation(Context context){
            this.mContext = context;
        }

        public boolean isConnectingToInternet(){

            ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected() == true)
            {
                return true;
            }

            return false;

        }


//    public  boolean isConnectingToInternet() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        if ((connectivityManager
//                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
//                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
//                || (connectivityManager
//                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
//                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//                .getState() == NetworkInfo.State.CONNECTED)) {
//            return true;
//        } else {
//            return false;
//        }
//    }

}