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

        int i = 0;

        // GETS CALLED! WHEE!
        AudioManager x = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        x.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        int originalMode = x.getRingerMode();

    }
}
