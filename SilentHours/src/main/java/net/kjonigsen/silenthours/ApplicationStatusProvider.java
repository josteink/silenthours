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
            Date now = new Date();
            return getFor(prefs, now);
        }
    }

    private static ApplicationStatus getFor(ApplicationPreferences prefs, Date now)
    {
        switch (prefs.OperationMode)
        {
            case ApplicationPreferences.OM_AllDaysWeekdays:
                return getForWeekday(prefs, now);

            case ApplicationPreferences.OM_AllDaysWeekends:
                return getForWeekend(prefs, now);

            default:
                return getForCurrent(prefs, now);
        }
    }

    private static ApplicationStatus getForCurrent(ApplicationPreferences prefs, Date now)
    {
        if (DateUtil.isWeekday(now))
        {
            return getForWeekday(prefs, now);
        }
        else
        {
            return getForWeekend(prefs, now);
        }
    }

    private static ApplicationStatus getForWeekday(ApplicationPreferences prefs, Date now)
    {
        return getFor(prefs, now, prefs.WeekdayStartTime, prefs.WeekdayStopTime);
    }

    private static ApplicationStatus getForWeekend(ApplicationPreferences prefs, Date now)
    {
        return getFor(prefs, now, prefs.WeekendStartTime, prefs.WeekendStopTime);
    }

    private static ApplicationStatus getFor(ApplicationPreferences prefs, Date now, Date start, Date end)
    {
        // dates instances normalized to match our now-instance's date-property, while maintainin their time-values.
        Date startNow = DateUtil.changeDate(start, now);
        Date endNow = DateUtil.changeDate(end, now);

        if (start.getTime() > end.getTime())
        {
            return getForOvernight(prefs, now, startNow, endNow);
        }
        else
        {
            return getForSameDay(prefs, now, startNow, endNow);
        }
    }

    private static ApplicationStatus getForOvernight(ApplicationPreferences prefs, Date now, Date start, Date end)
    {
        // pre midnight in the future as per today
        Date preMidnight = DateUtil.addDays(1,
                            DateUtil.addSeconds(-1,
                            DateUtil.getDateOnly(now)));

        // midnight in the past as per today
        Date midnight = DateUtil.getDateOnly(now);

        Date nextDate = new Date();
        nextDate.setTime(0);

        boolean enabledDay1 = DateUtil.isPast(start, now) && DateUtil.isFuture(preMidnight, now);
        if (enabledDay1)
        {
            // we dont know if tomorrow is weekend.
            // create a tomorrow-date and recurse.
            Date tomorrow = DateUtil.addDays(1,
                            DateUtil.getDateOnly(now));

            ApplicationStatus futureStatus = getFor(prefs, tomorrow);
            nextDate = futureStatus.NextApplicationEvent;
        }

        boolean enabledDay2 = DateUtil.isPast(midnight, now) && DateUtil.isFuture(end, now);
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

    private static ApplicationStatus getForSameDay(ApplicationPreferences prefs, Date now, Date start, Date end)
    {
        boolean enabled = DateUtil.isPast(start, now) && DateUtil.isFuture(end, now);

        ApplicationStatus result = new ApplicationStatus();
        result.ServiceEnabled = true;
        result.SilentHoursEnabled = enabled;
        result.NextApplicationEvent = enabled ? end : start;

        if (DateUtil.isPast(result.NextApplicationEvent, now))
        {
            // we should queue up for this time -tomorrow-
            result.NextApplicationEvent = DateUtil.addDays(1, result.NextApplicationEvent);
        }

        return result;
    }
}
