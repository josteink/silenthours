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

        boolean silentHoursEnabled = getEnabledStateFor(prefs);

        ApplicationStatus result = new ApplicationStatus();
        result.ServiceEnabled = prefs.ServiceEnabled;
        result.SilentHoursEnabled = silentHoursEnabled;

        return result;
    }

    private static boolean getEnabledStateFor(ApplicationPreferences prefs)
    {
        switch (prefs.OperationMode)
        {
            case ApplicationPreferences.OM_AllDaysWeekdays:
                return getEnabledStateForWeekday(prefs);

            case ApplicationPreferences.OM_AllDaysWeekends:
                return getEnabledStateForWeekend(prefs);

            default:
                return getEnabledStateForCurrent(prefs);
        }
    }

    private static boolean getEnabledStateForCurrent(ApplicationPreferences prefs)
    {
        if (isWeekday())
        {
            return getEnabledStateForWeekday(prefs);
        }
        else
        {
            return getEnabledStateForWeekend(prefs);
        }
    }

    private static boolean isWeekday()
    {
        Date now = new Date();
        int dayOfWeek = now.getDay();

        return (dayOfWeek > 0) && (dayOfWeek < 6);
    }

    private static boolean getEnabledStateForWeekday(ApplicationPreferences prefs)
    {
        return getEnabledStateFor(prefs.WeekdayStartTime, prefs.WeekdayStopTime);
    }

    private static boolean getEnabledStateForWeekend(ApplicationPreferences prefs)
    {
        return getEnabledStateFor(prefs.WeekendStartTime, prefs.WeekendStopTime);
    }

    private static boolean getEnabledStateFor(Date start, Date end)
    {
        if (start.getTime() > end.getTime())
        {
            return getEnabledStateForOvernight(start, end);
        }
        else
        {
            return getEnabledStateForSameDay(start, end);
        }
    }

    private static boolean getEnabledStateForOvernight(Date start, Date end)
    {
        Date now = new Date();
        Date preMidnight = new Date();
        preMidnight.setHours(23);
        preMidnight.setMinutes(59);
        preMidnight.setSeconds(59);

        Date midnight = new Date();
        midnight.setHours(0);
        midnight.setMinutes(0);
        midnight.setSeconds(0);

        boolean enabledDay1 =
                (now.getTime() > start.getTime()
                && now.getTime() < preMidnight.getTime());

        boolean enabledDay2 =
                (now.getTime() > midnight.getTime()
                && now.getTime() < end.getTime());

        return enabledDay1 || enabledDay2;

    }

    private static boolean getEnabledStateForSameDay(Date start, Date end)
    {
        Date now = new Date();

        return (now.getTime() > start.getTime()
            && now.getTime() < end.getTime());
    }
}
