package net.kjonigsen.silenthours;

import java.util.Date;

/**
 * Created by jostein on 22/09/13.
 */
public class ApplicationPreferences
{
    public static final int OM_AllDaysWeekdays = -1;
    public static final int OM_WeekdaysWeekends = 0;
    public static final int OM_AllDaysWeekends = 1;

    public boolean ServiceEnabled;
    public int OperationMode;
    public Date WeekdayStartTime;
    public Date WeekdayStopTime;
    public Date WeekendStartTime;
    public Date WeekendStopTime;
}
