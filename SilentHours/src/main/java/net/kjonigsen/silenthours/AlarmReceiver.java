package net.kjonigsen.silenthours;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;

/**
 * Created by jostein on 08/09/13.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ActivationManager.setStateFor(context);
        //ServiceManager.startStopService(context);
    }
}
