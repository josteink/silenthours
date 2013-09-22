package net.kjonigsen.silenthours;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

/**
 * Created by jostein on 22/09/13.
 */
public class ApplicationStatusProvider {

    private ApplicationStatusProvider()
    {
    }

    public static ApplicationStatus getFor(Context context)
    {
        ApplicationPreferences prefs = ApplicationPreferencesProvider.getPreferencesFor(context);

        Date nextDate = getNextApplicationEventDateFor(prefs);

        ApplicationStatus result = new ApplicationStatus();
        result.ServiceEnabled = prefs.ServiceEnabled;
        result.NextApplicationEvent = nextDate;

        return result;
    }

    private static Date getNextApplicationEventDateFor(ApplicationPreferences prefs)
    {
        switch (prefs.OperationMode)
        {
            case ApplicationPreferences.OM_AllDaysWeekdays:
                return getNextApplicationEventDateForWeekday(prefs);

            case ApplicationPreferences.OM_AllDaysWeekends:
                return getNextApplicationEventDateForWeekend(prefs);

            default:
                return getNextApplicationEventDateForCurrent(prefs);
        }
    }

    private static Date getNextApplicationEventDateForCurrent(ApplicationPreferences prefs)
    {
        if (isWeekday())
        {
            return getNextApplicationEventDateForWeekday(prefs);
        }
        else
        {
            return getNextApplicationEventDateForWeekend(prefs);
        }
    }

    private static Date getNextApplicationEventDateForWeekday(ApplicationPreferences prefs)
    {
        return getNextApplicationEventDateFor(prefs.WeekdayStartTime, prefs.WeekdayStopTime);
    }

    private static Date getNextApplicationEventDateForWeekend(ApplicationPreferences prefs)
    {
        return getNextApplicationEventDateFor(prefs.WeekendStartTime, prefs.WeekendStopTime);
    }

    private static Date getNextApplicationEventDateFor(Date start, Date end)
    {
        // identify which of these are in the future.

        Date now = new Date();

        if (now.getTime() > start.getTime())
        {
            return end;
        }
        else
        {
            return start;
        }
    }

    private static boolean isWeekday()
    {
        Date now = new Date();
        int dayOfWeek = now.getDay();

        return (dayOfWeek > 0) && (dayOfWeek < 6);
    }
}
