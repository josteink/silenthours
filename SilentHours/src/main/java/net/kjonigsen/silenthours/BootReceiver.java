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
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // initial reset may be needed on app start.
            ActivationManager.resetStateFor(context);
            ActivationManager.setStateFor(context, true);
        }
    }
}
