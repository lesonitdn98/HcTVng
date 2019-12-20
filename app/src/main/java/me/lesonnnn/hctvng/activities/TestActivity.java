package me.lesonnnn.hctvng.activities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Random;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.database.DatabaseHandler;
import me.lesonnnn.hctvng.models.ListTv;
import me.lesonnnn.hctvng.models.TuVung;

public class TestActivity extends BaseActivity implements View.OnClickListener {
    private ListTv mCategory;
    private List<TuVung> mTuVungs;
    private TuVung mTuVung;
    private TextView mTestTv;
    private View mBtnNext, mBtnSuggest;
    private boolean mCheck;

    @Override
    protected int initLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initComponents() {
        showNavLeft(R.drawable.ic_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        getData(intent.getIntExtra(DetailActivity.ID, 0));
        setTitle(mCategory.getTitle());
        mTestTv = findViewById(R.id.tvTestTv);
        mBtnNext = findViewById(R.id.btnNext);
        mBtnSuggest = findViewById(R.id.btnSuggest);
        ramdomTuVung();
    }

    @Override
    protected void addListener() {
        mBtnNext.setOnClickListener(this);
        mBtnSuggest.setOnClickListener(this);
    }

    private void getData(int id) {
        DatabaseHandler db = new DatabaseHandler(this);
        mCategory = db.getListTv(id);
        mTuVungs = db.getAllTuVungs(id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                ramdomTuVung();
                break;
            case R.id.btnSuggest:
                String text;
                if (mCheck) {
                    text = mTuVung.getNghia();
                } else {
                    text = mTuVung.getTu();
                }
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void ramdomTuVung() {
        Random rand = new Random();
        if (!(mTuVungs.size() == 0)) {
            int i = rand.nextInt(mTuVungs.size());
            int a = rand.nextInt(2);
            mTuVung = mTuVungs.get(i);
            if (a == 1) {
                mTestTv.setText(mTuVung.getTu());
                mCheck = true;
            } else if (a == 0) {
                mTestTv.setText(mTuVung.getNghia());
                mCheck = false;
            }
        }
    }
}
