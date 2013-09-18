package net.kjonigsen.silenthours;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jostein on 08/09/13.
 */
public class ServiceManager {

    public static void startStopService(Context context) {
        boolean enabled = getEnabled(context);
        Intent serviceIntent = new Intent(context, LocalService.class);

        if (enabled)
        {
            context.startService(serviceIntent);
        }
        else
        {
            context.stopService(serviceIntent);
        }
    }

    public static void stopService(Context context) {
        Intent serviceIntent = new Intent(context, LocalService.class);
        context.stopService(serviceIntent);
    }

    private static Boolean getEnabled(Context context)
    {
        String pref = "pref_force_landscape";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(pref, true);
    }
}
