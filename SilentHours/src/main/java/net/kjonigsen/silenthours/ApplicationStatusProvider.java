package net.kjonigsen.silenthours;

import android.content.Context;

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
        // pre midnight in the future as per today
        Date preMidnight = DateUtil.addDays(1,
                            DateUtil.addSeconds(-1,
                            DateUtil.getDateOnly(new Date())));

        // midnight in the past as per today
        Date midnight = DateUtil.getDateOnly(new Date());

        Date nextDate = new Date();
        nextDate.setTime(0);

        boolean enabledDay1 = DateUtil.isPast(start) && DateUtil.isFuture(preMidnight);
        if (enabledDay1)
        {
            // we dont know if tomorrow is weekend.
            // just force status update at (future) midnight when a new day is already here.
            Date futureMidnight = DateUtil.addDays(1,
                                    DateUtil.getDateOnly(new Date()));
            nextDate = futureMidnight;
        }

        boolean enabledDay2 = DateUtil.isPast(midnight) && DateUtil.isFuture(end);
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
        boolean enabled = DateUtil.isPast(start) && DateUtil.isFuture(end);

        ApplicationStatus result = new ApplicationStatus();
        result.ServiceEnabled = true;
        result.SilentHoursEnabled = enabled;
        result.NextApplicationEvent = enabled ? end : start;

        if (result.NextApplicationEvent.getTime() < now.getTime())
        {
            // we should queue up for this time -tomorrow-
            result.NextApplicationEvent = DateUtil.addDays(1, result.NextApplicationEvent);
        }

        return result;
    }
}
