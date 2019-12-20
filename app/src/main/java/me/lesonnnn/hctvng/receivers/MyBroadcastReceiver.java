package me.lesonnnn.hctvng.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import me.lesonnnn.hctvng.services.MyService;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyService.class);
        i.putExtra("id", intent.getIntExtra("id", 0));
        context.startService(i);
    }
}
