package net.kjonigsen.silenthours;

import java.util.Date;

/**
 * Created by jostein on 23/09/13.
 */
public class ServiceStatus {
    public boolean SilentHoursEnabled;
    public int OriginalRingerMode;
    public Date LastQueuedEvent;

    public void SafeReset()
    {
        SilentHoursEnabled = false;
        LastQueuedEvent = new Date(0);
        // keep ringermode untouched. we can't recover it.
    }
}
