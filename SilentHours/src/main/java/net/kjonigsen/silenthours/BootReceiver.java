package net.kjonigsen.silenthours;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jostein on 08/09/13.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // BOOT COMPLETED should start service.
        // http://stackoverflow.com/questions/5051687/broadcastreceiver-not-receiving-boot-completed
        // What else can we receive? Checks docs.

        ActivationManager.setStateFor(context);
        //ServiceManager.startStopService(context);
    }
}
