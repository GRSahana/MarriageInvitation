package com.ubikasoftwares.marriageinvitation.Utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class InternetConnection {

    public static boolean internetConnectionAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo == null) return false;

        switch (activeNetworkInfo.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                if ((activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED ||
                        activeNetworkInfo.getState() == NetworkInfo.State.CONNECTING))
                    return true;
                break;
            case ConnectivityManager.TYPE_MOBILE:
                if ((activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED ||
                        activeNetworkInfo.getState() == NetworkInfo.State.CONNECTING))
                    return true;
                break;
            default:
                return false;

        }
        return false;
    }

    public static boolean hasCheckPermission(final Context ctx){
        int hasWriteContactsPermission = checkSelfPermission(ctx, Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
        return  true;
    }


}
