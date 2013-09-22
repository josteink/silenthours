package net.kjonigsen.silenthours;

import java.util.Date;

/**
 * Created by jostein on 22/09/13.
 */
public class ApplicationStatus {

    public boolean ServiceEnabled;
    public int CurrentProfile;
    public Date NextApplicationEvent;

    public String getCurrentProfileName()
    {
        switch (CurrentProfile)
        {
            case ApplicationPreferences.OM_AllDaysWeekdays:
                return "All days weekdays.";

            case ApplicationPreferences.OM_AllDaysWeekends:
                return "All days weekends.";

            default:
                return "Weekdays and weekends.";
        }
    }
}
