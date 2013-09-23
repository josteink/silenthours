package net.kjonigsen.silenthours;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jostein on 23/09/13.
 */
public class ServiceStatusProvider {

    private final static String SSP_PreferenceKey = "ServiceStatus";
    private final static String SSP_SilentHoursEnabled = "SilentHoursEnabled";
    private final static String SSP_OriginalRingerMode = "OriginalRingerMode";

    private ServiceStatusProvider()
    {
    }

    public static ServiceStatus getFor(Context context)
    {
        SharedPreferences prefs = getPrefsFor(context);

        ServiceStatus result = new ServiceStatus();
        result.SilentHoursEnabled = prefs.getBoolean(SSP_SilentHoursEnabled, false);
        result.OriginalRingerMode = prefs.getInt(SSP_OriginalRingerMode, 0);

        return result;
    }

    public static void setFor(Context context, ServiceStatus status)
    {
        SharedPreferences prefs = getPrefsFor(context);

        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.putBoolean(SSP_SilentHoursEnabled, status.SilentHoursEnabled);
        edit.putInt(SSP_OriginalRingerMode, status.OriginalRingerMode);
        edit.commit();
    }

    private static SharedPreferences getPrefsFor(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(SSP_PreferenceKey, context.MODE_PRIVATE);
        return prefs;
    }
}
