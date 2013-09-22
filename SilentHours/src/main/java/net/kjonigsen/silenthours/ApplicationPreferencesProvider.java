package net.kjonigsen.silenthours;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

/**
 * Created by jostein on 22/09/13.
 */
public class ApplicationPreferencesProvider {

    private ApplicationPreferencesProvider()
    {
    }

    public static ApplicationPreferences getPreferencesFor(Context context)
    {
        ApplicationPreferences result = new ApplicationPreferences();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        result.ServiceEnabled = prefs.getBoolean("enable_service", true);
        result.OperationMode = Integer.parseInt(prefs.getString("operation_mode", "0"));

        result.WeekdayStartTime = getDateForKeys(prefs, "weekdays_hours_start", "weekdays_minutes_start");
        result.WeekdayStopTime = getDateForKeys(prefs, "weekdays_hours_stop", "weekdays_minutes_stop");

        result.WeekendStartTime = getDateForKeys(prefs, "weekends_hours_start", "weekends_minutes_start");
        result.WeekendStopTime = getDateForKeys(prefs, "weekends_hours_stop", "weekends_minutes_stop");

        return result;
    }

    private static Date getDateForKeys(SharedPreferences prefs, String hourKey, String minuteKey)
    {
        int hour = Integer.parseInt(prefs.getString(hourKey, "0"));
        int minute = Integer.parseInt(prefs.getString(minuteKey, "0"));

        Date result = new Date();
        result.setHours(hour);
        result.setMinutes(minute);
        result.setSeconds(0);

        return result;
    }

}
