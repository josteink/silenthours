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
        if (!prefs.ServiceEnabled)
        {
            ApplicationStatus result = new ApplicationStatus();
            result.ServiceEnabled = false;
            result.SilentHoursEnabled = false;

            Date dummyDate = new Date();
            dummyDate.setTime(0);
            result.NextApplicationEvent = dummyDate;
            return result;
        }
        else
        {
            return getFor(prefs);
        }
    }

    private static ApplicationStatus getFor(ApplicationPreferences prefs)
    {
        switch (prefs.OperationMode)
        {
            case ApplicationPreferences.OM_AllDaysWeekdays:
                return getForWeekday(prefs);

            case ApplicationPreferences.OM_AllDaysWeekends:
                return getForWeekend(prefs);

            default:
                return getForCurrent(prefs);
        }
    }

    private static ApplicationStatus getForCurrent(ApplicationPreferences prefs)
    {
        if (isWeekday())
        {
            return getForWeekday(prefs);
        }
        else
        {
            return getForWeekend(prefs);
        }
    }

    private static boolean isWeekday()
    {
        Date now = new Date();
        int dayOfWeek = now.getDay();

        return (dayOfWeek > 0) && (dayOfWeek < 6);
    }

    private static ApplicationStatus getForWeekday(ApplicationPreferences prefs)
    {
        return getFor(prefs.WeekdayStartTime, prefs.WeekdayStopTime);
    }

    private static ApplicationStatus getForWeekend(ApplicationPreferences prefs)
    {
        return getFor(prefs.WeekendStartTime, prefs.WeekendStopTime);
    }

    private static ApplicationStatus getFor(Date start, Date end)
    {
        if (start.getTime() > end.getTime())
        {
            return getForOvernight(start, end);
        }
        else
        {
            return getForSameDay(start, end);
        }
    }

    private static ApplicationStatus getForOvernight(Date start, Date end)
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

        Date nextDate = new Date();
        nextDate.setTime(0);

        boolean enabledDay1 =
                (now.getTime() > start.getTime()
                && now.getTime() < preMidnight.getTime());
        if (enabledDay1)
        {
            long oneDay = 24 * 60 * 60 * 1000L;
            Date endTomorrow = new Date();
            endTomorrow.setTime(end.getTime() + oneDay);
            nextDate = endTomorrow;
        }

        boolean enabledDay2 =
                (now.getTime() > midnight.getTime()
                && now.getTime() < end.getTime());
        if (enabledDay2)
        {
            nextDate = end;
        }

        boolean enabled = enabledDay1 || enabledDay2;
        if (!enabled)
        {
            nextDate = start;
        }

        ApplicationStatus result = new ApplicationStatus();
        result.ServiceEnabled = true;
        result.SilentHoursEnabled = enabled;
        result.NextApplicationEvent = nextDate;

        return result;
    }

    private static ApplicationStatus getForSameDay(Date start, Date end)
    {
        Date now = new Date();
        boolean enabled = (now.getTime() > start.getTime()
                && now.getTime() < end.getTime());

        ApplicationStatus result = new ApplicationStatus();
        result.ServiceEnabled = true;
        result.SilentHoursEnabled = enabled;
        result.NextApplicationEvent = enabled ? end : start;

        return result;
    }
}
