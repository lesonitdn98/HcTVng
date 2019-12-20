package me.lesonnnn.hctvng.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.database.DatabaseHandler;
import me.lesonnnn.hctvng.models.TuVung;
import me.lesonnnn.hctvng.receivers.MyBroadcastReceiver;

public class MyService extends Service {
    private final IBinder mBinder = new LocalBinder();

    private static final String CHANNEL_ID = "CHANNEL";
    private int mId;
    private Timer mTimer = new Timer();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NAME";
            String description = "DESCRIPTION";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        mId = intent.getIntExtra("id", 0);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ramdom(mId);
            }
        }, 0, 10000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showNotification(String tuvung, String noidung) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_laucher)
                        .setContentTitle(tuvung)
                        .setContentText(noidung)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        int notificationId = 1111;
        notificationManager.notify(notificationId, builder.build());
    }

    private void ramdom(int id) {
        DatabaseHandler db = new DatabaseHandler(this);
        List<TuVung> tuVungs = db.getAllTuVungs(id);
        Random rand = new Random();
        int i = rand.nextInt(tuVungs.size());
        TuVung tuVung = tuVungs.get(i);
        showNotification(tuVung.getTu(), tuVung.getNghia());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent broadcastIntent = new Intent(this, MyBroadcastReceiver.class);
        broadcastIntent.putExtra("id", mId);
        sendBroadcast(broadcastIntent);
    }

    public void setId(int id) {
        mId = id;
    }

    public void stopNoti() {
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(MyService.this);
        notificationManager.cancel(1111);
        mTimer.cancel();
        mTimer.purge();
    }
}
