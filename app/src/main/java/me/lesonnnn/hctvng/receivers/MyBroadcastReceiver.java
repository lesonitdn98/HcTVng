package me.lesonnnn.hctvng.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import me.lesonnnn.hctvng.services.MyService;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        Intent i = new Intent(context, MyService.class);
        if (id != -1) {
            i.putExtra("id", id);
            context.startService(i);
        } else {
            context.stopService(i);
        }
    }
}
