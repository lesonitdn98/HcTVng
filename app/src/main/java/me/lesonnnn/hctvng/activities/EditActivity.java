package me.lesonnnn.hctvng.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import java.util.List;
import java.util.Objects;
import me.lesonnnn.hctvng.R;
import me.lesonnnn.hctvng.adapters.ListTuVungAdapter;
import me.lesonnnn.hctvng.database.DatabaseHandler;
import me.lesonnnn.hctvng.models.ListTv;
import me.lesonnnn.hctvng.models.TuVung;

public class EditActivity extends BaseActivity implements View.OnClickListener {
    public static final int RESULT_EDIT = 1234;
    public static final int RESULT_DELETE = 1337;
    private EditText mEdtTitle;
    private View mBtnAddTv;
    private ListTv mCategory;
    private List<TuVung> mTuVungs, mAddTuVungs;
    private RecyclerView rvListTv;
    private EditText mEdtTuMoi, mEdtNoiDung;
    private ListTuVungAdapter mTuVungAdapter;
    private LinearLayout mBtnDelete;

    @Override
    protected int initLayout() {
        return R.layout.activity_add;
    }

    @Override
    protected void initComponents() {
        setTitle(getString(R.string.chinh_sua));
        showNavLeft(R.drawable.ic_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        showNavRight(R.drawable.ic_done, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEdtTitle.getText().toString().trim().equals("")) {
                    DatabaseHandler db = new DatabaseHandler(EditActivity.this);
                    db.updateCategory(
                            new ListTv(mCategory.getId(), mEdtTitle.getText().toString().trim()));
                    for (TuVung tuVung : mAddTuVungs) {
                        tuVung.setIdListTV(mCategory.getId());
                        db.addTuVung(tuVung);
                    }
                    setResult(RESULT_EDIT);
                    finish();
                } else {
                    Toast.makeText(EditActivity.this, "Thiếu tiêu đề kìa!!!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        Intent intent = getIntent();
        getData(intent.getIntExtra(DetailActivity.ID, 0));

        mAddTuVungs = new ArrayList<>();
        mEdtTitle = findViewById(R.id.edtTitle);
        mEdtTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        rvListTv = findViewById(R.id.rvTuVung);
        mBtnAddTv = findViewById(R.id.btnAddTV);
        mBtnDelete = findViewById(R.id.lnDelete);
        mBtnDelete.setVisibility(View.VISIBLE);

        mEdtTitle.setText(mCategory.getTitle());
        rvListTv.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mTuVungAdapter = new ListTuVungAdapter(mTuVungs);
        rvListTv.setAdapter(mTuVungAdapter);
    }

    @Override
    protected void addListener() {
        mBtnAddTv.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
    }

    private void getData(int id) {
        DatabaseHandler db = new DatabaseHandler(this);
        mCategory = db.getListTv(id);
        mTuVungs = db.getAllTuVungs(id);
    }

    private void showAddDialog() {
        final EditActivity.CustomAddTvDialog dialog = new EditActivity.CustomAddTvDialog(this);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddTV:
                showAddDialog();
                break;
            case R.id.lnDelete:
                DatabaseHandler db = new DatabaseHandler(this);
                db.deleteTuVungs(mCategory);
                db.deleteCategory(mCategory);
                setResult(RESULT_DELETE);
                finish();
                break;
        }
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
                    EditActivity.CustomAddTvDialog.this.dismiss();
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
                        mAddTuVungs.add(new TuVung(mEdtTuMoi.getText().toString().trim(),
                                mEdtNoiDung.getText().toString().trim()));
                        rvListTv.scrollToPosition(mTuVungs.size() - 1);
                        mTuVungAdapter.notifyDataSetChanged();
                        EditActivity.CustomAddTvDialog.this.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Hơi thiếu!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
