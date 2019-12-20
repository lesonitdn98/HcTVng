package me.lesonnnn.hctvng.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.adapters.ListTuVungAdapter;
import me.lesonnnn.hctvng.database.DatabaseHandler;
import me.lesonnnn.hctvng.models.TuVung;
import me.lesonnnn.hctvng.services.MyService;
import me.lesonnnn.hctvng.services.MyService.LocalBinder;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String ID = "id";
    public static final int REQUEST_CODE = 1111;
    public static final int RESULT_UPDATE = 2222;
    private MyService mService;
    private boolean mBound = false;
    private List<TuVung> mTuVungs;
    private ListTuVungAdapter mTuVungAdapter;
    private int mCategoryId;
    private LinearLayout mPlay;

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initComponents() {
        Intent intent = getIntent();
        mCategoryId = intent.getIntExtra("categoryId", 0);

        showNavLeft(R.drawable.ic_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        showNavRight(R.drawable.ic_edit, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                intent.putExtra(ID, mCategoryId);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        showNavRight2(MainActivity.CHECK_NOTI && MainActivity.NOTI_ID == mCategoryId
                ? R.drawable.ic_noti_run : R.drawable.ic_noti, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.CHECK_NOTI && MainActivity.NOTI_ID == mCategoryId) {
                    setIconNavRight2(R.drawable.ic_noti);
                    MainActivity.CHECK_NOTI = false;
                    MainActivity.NOTI_ID = -1;
                    mService.stopNoti();
                } else if (MainActivity.CHECK_NOTI) {
                    setIconNavRight2(R.drawable.ic_noti_run);
                    MainActivity.CHECK_NOTI = true;
                    MainActivity.NOTI_ID = mCategoryId;
                    mService.setId(mCategoryId);
                } else {
                    setIconNavRight2(R.drawable.ic_noti_run);
                    MainActivity.CHECK_NOTI = true;
                    MainActivity.NOTI_ID = mCategoryId;
                    Intent intent = new Intent(DetailActivity.this, MyService.class);
                    intent.putExtra("id", mCategoryId);
                    startService(intent);
                }
            }
        });

        mPlay = findViewById(R.id.lnPlay);

        RecyclerView rvTuVungs = findViewById(R.id.rv_TuVung);
        DatabaseHandler db = new DatabaseHandler(this);

        setTitle(db.getListTv(mCategoryId).

                getTitle());
        mTuVungs = db.getAllTuVungs(mCategoryId);

        rvTuVungs.setLayoutManager(new

                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mTuVungAdapter = new

                ListTuVungAdapter(mTuVungs);
        rvTuVungs.setAdapter(mTuVungAdapter);
    }

    @Override
    protected void addListener() {
        mPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.lnPlay) {
            Intent intent = new Intent(DetailActivity.this, TestActivity.class);
            intent.putExtra(ID, mCategoryId);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == EditActivity.RESULT_EDIT) {
                getData();
                setResult(RESULT_UPDATE);
            }
            if (resultCode == EditActivity.RESULT_DELETE) {
                setResult(RESULT_UPDATE);
                finish();
            }
        }
    }

    private void getData() {
        DatabaseHandler db = new DatabaseHandler(this);
        setTitle(db.getListTv(mCategoryId).getTitle());
        mTuVungs = db.getAllTuVungs(mCategoryId);
        mTuVungAdapter.updateData(mTuVungs);
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
}
