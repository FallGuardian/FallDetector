package multiple.by.patience;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @SuppressLint("Wakelock")
	@Override
    public void onReceive(Context context, Intent intent) {
    	PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        wl.acquire();
    	Toast.makeText(context, "這是一個Toast......", Toast.LENGTH_LONG).show();
    	

        wl.release();
    }
    
}
