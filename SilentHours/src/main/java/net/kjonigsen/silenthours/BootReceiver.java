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

        // use alarmmanager:
        // http://stackoverflow.com/questions/4452565/start-app-at-a-specific-time

        ServiceManager.startStopService(context);

        /*

        ca:
          1a: app start
          1b: boot completed broadcast
          =>
          2: start service
          2.5: hente config,

          3: service registrerer med en alarm manager en intent

          4: intent = lydløs eller ikke. (med notification!)

          annet:
          1. må støtte kansellering?
             => stoppe service
          2. endre config
             => stoppe service & restarte.

         */
    }
}
