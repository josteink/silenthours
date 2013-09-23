package net.kjonigsen.silenthours;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

/**
 * Created by jostein on 23/09/13.
 */
public class ActivationManager {

    private ActivationManager()
    {
    }

    private static boolean _isInitialized;
    private static AlarmManager _alarmManager;
    private static NotificationManager _notificationManager;
    private static AudioManager _audioManager;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private final static int NOTIFICATION = R.string.service_started;

    private static void InitializeFor(Context context)
    {
        if (!_isInitialized)
        {
            _notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            _alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            _audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            _isInitialized = true;
        }
    }

    public static void resetStateFor(Context context)
    {
        ServiceStatus status = new ServiceStatus();
        ServiceStatusProvider.setFor(context, status);
    }

    public static void setStateFor(Context context)
    {
        InitializeFor(context);

        ApplicationStatus applicationStatus = ApplicationStatusProvider.getFor(context);
        if (applicationStatus.ServiceEnabled)
        {
            // only queue new events if we are enabled.
            QueueNextAlarm(context, applicationStatus);
        }

        ServiceStatus serviceStatus = ServiceStatusProvider.getFor(context);
        boolean stateModified = UpdateServiceStatus(context, applicationStatus, serviceStatus);
        if (stateModified)
        {
            ServiceStatusProvider.setFor(context, serviceStatus);
        }
    }

    private static void QueueNextAlarm(Context context, ApplicationStatus status)
    {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long millis = status.NextApplicationEvent.getTime();
        _alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
    }

    private static boolean UpdateServiceStatus(Context context, ApplicationStatus appStatus, ServiceStatus srvStatus)
    {
        boolean stateModified = false;
        if (appStatus.SilentHoursEnabled && !srvStatus.SilentHoursEnabled)
        {
            enableSilentHours(srvStatus);
            showNotification(context);
            stateModified = true;
        }
        else if (!appStatus.SilentHoursEnabled && srvStatus.SilentHoursEnabled)
        {
            disableSilentHours(srvStatus);
            cancelNotification();
            stateModified = true;
        }
        return stateModified;
    }

    private static void enableSilentHours(ServiceStatus srvStatus)
    {
        srvStatus.OriginalRingerMode = _audioManager.getRingerMode();
        _audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        srvStatus.SilentHoursEnabled = true;
    }

    private static void disableSilentHours(ServiceStatus srvStatus)
    {
        _audioManager.setRingerMode(srvStatus.OriginalRingerMode);
        srvStatus.SilentHoursEnabled = false;
    }

    private static void showNotification(Context context)
    {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = context.getText(R.string.service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.cat_head, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(context, context.getText(R.string.local_service_label),
                text, contentIntent);

        // Send the notification.
        _notificationManager.notify(NOTIFICATION, notification);
    }

    private static void cancelNotification()
    {
        _notificationManager.cancel(NOTIFICATION);
    }
}
