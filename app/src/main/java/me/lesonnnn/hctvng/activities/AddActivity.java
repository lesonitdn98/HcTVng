package me.lesonnnn.hctvng.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.adapters.ListTuVungAdapter;
import me.lesonnnn.hctvng.database.DatabaseHandler;
import me.lesonnnn.hctvng.models.ListTv;
import me.lesonnnn.hctvng.models.TuVung;

public class AddActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEdtTitle;
    private View mBtnAddTv;
    private ArrayList<TuVung> mTuVungs;
    private RecyclerView rvListTv;
    private ListTuVungAdapter mTuVungAdapter;
    private EditText mEdtTuMoi, mEdtNoiDung;

    @Override
    protected int initLayout() {
        return R.layout.activity_add;
    }

    @Override
    protected void initComponents() {
        setTitle(getString(R.string.th_m_danh_s_ch));
        showNavLeft(R.drawable.ic_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        showNavRight(R.drawable.ic_done, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEdtTitle.getText().toString().trim().equals("") && mTuVungs.size() != 0) {
                    DatabaseHandler db = new DatabaseHandler(AddActivity.this);
                    int idListTv =
                            db.addCategory(new ListTv(mEdtTitle.getText().toString().trim()));
                    for (TuVung tuVung : mTuVungs) {
                        tuVung.setIdListTV(idListTv);
                        db.addTuVung(tuVung);
                    }
                    setResult(MainActivity.RESULT_CODE);
                    onBackPressed();
                } else {
                    Toast.makeText(AddActivity.this, "Có vẻ hơi thiếu thiếu!!!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        mEdtTitle = findViewById(R.id.edtTitle);
        mEdtTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        rvListTv = findViewById(R.id.rvTuVung);
        mBtnAddTv = findViewById(R.id.btnAddTV);

        rvListTv.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mTuVungs = new ArrayList<>();
        mTuVungAdapter = new ListTuVungAdapter(mTuVungs);
        rvListTv.setAdapter(mTuVungAdapter);
    }

    @Override
    protected void addListener() {
        mBtnAddTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAddTV) {
            showAddDialog();
        }
    }

    private void showAddDialog() {
        final CustomAddTvDialog dialog = new CustomAddTvDialog(this);
        dialog.show();
    }

    public class CustomAddTvDialog extends Dialog {

        CustomAddTvDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_addtuvung);
            Window window = this.getWindow();
            assert window != null;
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            Objects.requireNonNull(getWindow())
                    .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.getWindow().setAttributes(wlp);
            init();
        }

        private void init() {
            LinearLayout btnCancel = findViewById(R.id.tab_cancel);
            LinearLayout btnDone = findViewById(R.id.tab_done);
            mEdtTuMoi = findViewById(R.id.etTumoi);
            mEdtNoiDung = findViewById(R.id.etNoidung);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomAddTvDialog.this.dismiss();
                }
            });
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mEdtTuMoi.getText().toString().trim().equals("") && !mEdtNoiDung.getText()
                            .toString()
                            .trim()
                            .equals("")) {
                        mTuVungs.add(new TuVung(mEdtTuMoi.getText().toString().trim(),
                                mEdtNoiDung.getText().toString().trim()));
                        rvListTv.scrollToPosition(mTuVungs.size() - 1);
                        mTuVungAdapter.notifyDataSetChanged();
                        CustomAddTvDialog.this.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Hơi thiếu!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
