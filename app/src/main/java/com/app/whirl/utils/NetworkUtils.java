package com.app.whirl.utils;



import android.content.Context;
import android.net.ConnectivityManager;

import com.app.whirl.R;


public class NetworkUtils {
    public static boolean checkInternetConnection(Context context) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // ARE WE CONNECTED TO THE NET
            if (conMgr.getActiveNetworkInfo() != null

                    && conMgr.getActiveNetworkInfo().isAvailable()

                    && conMgr.getActiveNetworkInfo().isConnected()) {

                return true;

            } else {
                ToastUtil.toast_Long(context, context.getResources().getString(R.string.No_Internet));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
